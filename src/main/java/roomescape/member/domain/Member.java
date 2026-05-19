package roomescape.member.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;

    public Member(String email, String password, String name) {
        this(null, email, password, name);
    }

    private Member(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member withId(Long id) {
        return new Member(id, this.email, this.password, this.name);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}