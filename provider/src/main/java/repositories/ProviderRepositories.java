package repositories;

import entity.ProviderEntity;
import io.vertx.core.Future;
import java.util.List;

public interface ProviderRepositories {

  Future<List<ProviderEntity>> getProviders();

  Future<ProviderEntity> findProviderById(String id);

  Future<Void> insertProvider(ProviderEntity providerEntity);

  Future<Void> updateProvider(String id,ProviderEntity providerEntity);

  Future<Void> deleteProvider(String id);
}
