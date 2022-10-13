package repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface CRUDRepositoriesInterface<T> {
    Single<List<T>> getAll();
    Single<T> findById(String id);
    Single<String> insert(T entity);
    Single<T> update(String id, T entity);
    Completable delete(String id);
}
