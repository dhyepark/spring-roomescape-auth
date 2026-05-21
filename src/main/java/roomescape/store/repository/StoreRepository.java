package roomescape.store.repository;

import java.util.Optional;

import roomescape.store.domain.Store;

public interface StoreRepository {
    Optional<Store> findByManagerId(Long managerId);
    Store findById(Long id);
}