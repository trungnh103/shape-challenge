package com.trungnh103.shapeChallenge.repository;

import com.mongodb.MongoClient;
import com.trungnh103.shapeChallenge.model.Category;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

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
    public void givenCategoryObject_whenSave_thenCreateNewCategory() {
        Category category = new Category();
        category.setName("CIRCLE");
        category.setRequirements(Arrays.asList("radius"));
        categoryRepository.save(category);
        assertThat(categoryRepository.findAll().size()).isGreaterThan(0);
    }

    @Test
    public void givenCategoryObject_whenSave_thenFindByName() {
        String categoryName = "CIRCLE 2";
        Category category = new Category();
        category.setName(categoryName);
        category.setRequirements(Arrays.asList("radius"));
        categoryRepository.save(category);
        Category found = categoryRepository.findByName(categoryName);
        assertThat(found.getName()).isEqualTo(categoryName);
        assertThat(found.getRequirements()).contains("radius");
    }
}
