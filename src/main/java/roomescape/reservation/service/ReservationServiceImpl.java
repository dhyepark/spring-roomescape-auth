package roomescape.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.service.TimeService;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            TimeService timeService,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    @Override
    public Reservation create(ReservationSaveServiceDto dto) {
        ReservationTime time = findTime(dto.timeId());
        Long themeId = dto.themeId();
        Long memberId = dto.memberId();
        time.validateReservableSchedule();
        validateThemeId(themeId);
        validateMemberId(memberId);
        validateDuplicatedReservation(themeId, time);
        Reservation saved = reservationRepository.save(new Reservation(memberId, time, themeId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        return saved.withTheme(themeRepository.findById(themeId)).withMember(member);
    }

    @Override
    public void cancel(Long id) {
        boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new ReservationNotFoundException(id);
        }
    }

    @Override
    public List<Reservation> getByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId);
    }

    @Override
    public void cancelForUser(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservation.getTime().validateNotPastForCancel();
        reservationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Reservation update(Long id, Long timeId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservation.getTime().validateUpdatableReservation();
        ReservationTime newTime = findTime(timeId);
        newTime.validateReservableSchedule();
        validateDuplicatedReservation(reservation.getThemeId(), newTime);
        reservationRepository.update(id, timeId);
        return reservation.withTime(newTime);
    }

    private void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
        if (!themeRepository.existsById(themeId)) {
            throw new ThemeNotFoundException(themeId);
        }
    }

    private void validateMemberId(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("회원은 필수입니다.");
        }
        if (!memberRepository.findById(memberId).isPresent()) {
            throw new MemberNotFoundException(memberId);
        }
    }

    private void validateDuplicatedReservation(Long themeId, ReservationTime time) {
        if (reservationRepository.isDuplicated(themeId, time)) {
            throw new DuplicateReservationException();
        }
    }

    private ReservationTime findTime(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        return timeService.findById(timeId);
    }
}