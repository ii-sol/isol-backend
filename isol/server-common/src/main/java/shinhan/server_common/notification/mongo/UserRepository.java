package shinhan.server_common.notification.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import shinhan.server_common.domain.entity.TempUser;

public interface UserRepository extends MongoRepository<TempUser, Integer> {

}
