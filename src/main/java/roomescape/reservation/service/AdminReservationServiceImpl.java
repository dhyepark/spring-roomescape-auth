package roomescape.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.auth.exception.ForbiddenException;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.store.domain.Store;
import roomescape.store.exception.StoreNotFoundException;
import roomescape.store.repository.StoreRepository;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.service.TimeService;

@Service
public class AdminReservationServiceImpl implements AdminReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final TimeService timeService;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public AdminReservationServiceImpl(
            ReservationRepository reservationRepository,
            StoreRepository storeRepository,
            TimeService timeService,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.storeRepository = storeRepository;
        this.timeService = timeService;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Reservation> getByManagerId(Long managerId) {
        Store store = findStore(managerId);
        return reservationRepository.findAllByStoreId(store.getId());
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
    public void cancel(Long id, Long managerId) {
        Store store = findStore(managerId);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        if (!reservation.belongsToStore(store.getId())) {
            throw new ForbiddenException();
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Reservation update(Long id, Long timeId, Long managerId) {
        Store store = findStore(managerId);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        if (!reservation.belongsToStore(store.getId())) {
            throw new ForbiddenException();
        }
        ReservationTime newTime = findTime(timeId);
        newTime.validateReservableSchedule();
        validateDuplicatedReservation(reservation.getThemeId(), newTime);
        boolean updated = reservationRepository.update(id, timeId);
        if (!updated) {
            throw new IllegalStateException("예약 수정에 실패했습니다. id: " + id);
        }
        return reservation.withTime(newTime);
    }

    private Store findStore(Long managerId) {
        return storeRepository.findByManagerId(managerId)
                .orElseThrow(() -> new StoreNotFoundException(managerId));
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
        if (memberRepository.findById(memberId).isEmpty()) {
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
