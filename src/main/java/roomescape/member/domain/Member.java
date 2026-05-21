package roomescape.member.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Role role;

    public Member(String email, String password, String name) {
        this(null, email, password, name, Role.USER);
    }

    public Member(String email, String password, String name, Role role) {
        this(null, email, password, name, role);
    }

    private Member(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member withId(Long id) {
        return new Member(id, this.email, this.password, this.name, this.role);
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

    public Role getRole() {
        return role;
    }

    public boolean isManager() {
        return role == Role.MANAGER;
    }
}