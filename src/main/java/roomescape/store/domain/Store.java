package roomescape.store.domain;

public class Store {
    private final Long id;
    private final String name;
    private final Long managerId;

    public Store(String name, Long managerId) {
        this(null, name, managerId);
    }

    private Store(Long id, String name, Long managerId) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
    }

    public Store withId(Long id) {
        return new Store(id, this.name, this.managerId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getManagerId() {
        return managerId;
    }
}