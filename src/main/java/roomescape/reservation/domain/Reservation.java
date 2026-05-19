package roomescape.reservation.domain;

import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;

public class Reservation {
    private final Long id;
    private final Long memberId;
    private final Member member;
    private final ReservationTime time;
    private final Long themeId;
    private final Theme theme;

    public Reservation(Long memberId, ReservationTime time, Long themeId) {
        this(null, memberId, null, time, themeId, null);
    }

    private Reservation(Long id, Long memberId, Member member, ReservationTime time, Long themeId, Theme theme) {
        this.id = id;
        this.memberId = memberId;
        this.member = member;
        this.time = time;
        this.themeId = themeId;
        this.theme = theme;
    }

    public Reservation withId(Long id) {
        return new Reservation(id, memberId, member, time, themeId, theme);
    }

    public Reservation withMember(Member member) {
        return new Reservation(id, memberId, member, time, themeId, theme);
    }

    public Reservation withTheme(Theme theme) {
        return new Reservation(id, memberId, member, time, themeId, theme);
    }

    public Reservation withTime(ReservationTime time) {
        return new Reservation(id, memberId, member, time, themeId, theme);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Member getMember() {
        return member;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Theme getTheme() {
        return theme;
    }
}