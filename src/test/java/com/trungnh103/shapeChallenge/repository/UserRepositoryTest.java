package com.trungnh103.shapeChallenge.repository;

import com.mongodb.MongoClient;
import com.trungnh103.shapeChallenge.model.User;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;

    @AfterEach
    void clean() {
        mongodExecutable.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        String ip = "localhost";
        int randomPort = SocketUtils.findAvailableTcpPort();

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, randomPort, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(new MongoClient(ip, randomPort), "test");
    }


    @Test
    public void givenUserObject_whenSave_thenCreateNewUser() {
        User user = new User("KID 1","p@ssw0rd");
        user.setActive(true);
        user.setEmail("kid@abc");
        user.setPhoneNumber("09888");
        userRepository.save(user);
        assertThat(userRepository.findAll().size()).isGreaterThan(0);
    }

    @Test
    public void givenUserObject_whenSave_thenFindByActive() {
        String userName = "KID 2";
        User user = new User(userName,"p@ssw0rd");
        user.setActive(true);
        user.setEmail("kid@abc");
        user.setPhoneNumber("09888");
        userRepository.save(user);
        assertThat(userRepository.findByActive(true)).contains(user);
    }

    @Test
    public void givenUserObject_whenSave_thenFindByUserName() {
        String userName = "SUPER KID";
        User user = new User(userName,"p@ssw0rd");
        user.setActive(true);
        user.setEmail("kid@abc");
        user.setPhoneNumber("09888");
        userRepository.save(user);
        assertThat(userRepository.findByUsername(userName)).isEqualTo(user);
    }
}
