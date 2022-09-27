package repositories;

import entity.ProviderEntity;
import io.vertx.core.Future;
import java.util.List;

public interface ProviderRepositories {

  Future<List<ProviderEntity>> getProviders();

  Future<ProviderEntity> findProviderById(String id);

  Future<String> insertProvider(ProviderEntity userEntity);

  Future<String> updateProvider(String id, ProviderEntity userEntity);

  Future<String> deleteProvider(String id);
}
