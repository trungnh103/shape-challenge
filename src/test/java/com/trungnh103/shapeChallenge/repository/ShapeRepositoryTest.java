package com.trungnh103.shapeChallenge.repository;

import com.mongodb.MongoClient;
import com.trungnh103.shapeChallenge.model.Category;
import com.trungnh103.shapeChallenge.model.Shape;
import com.trungnh103.shapeChallenge.model.ShapeProperty;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.assertj.core.util.Sets;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class ShapeRepositoryTest {
    @Autowired
    ShapeRepository shapeRepository;
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
    public void givenShapeObject_whenSave_thenCreateNewShape() {
        Category category = new Category();
        category.setName("SQUARE");
        Shape shape = new Shape(category, "TOKYO SQUARE");
        shape.setUsername("SUPER KID");
        category.setRequirements(Arrays.asList("size"));
        category.setId("1");
        shape.setCategory(category);
        ShapeProperty shapeProperty = new ShapeProperty("size", "9");
        Set<ShapeProperty> shapeProperties = new HashSet<>();
        shapeProperties.add(shapeProperty);
        shape.setShapeProperties(shapeProperties);
        categoryRepository.save(category);
        shapeRepository.save(shape);
        assertThat(shapeRepository.findAll().size()).isGreaterThan(0);
    }

    @Test
    public void givenShapeObject_whenSave_thenFindByUserName() {
        String shapeName = "TOKYO SQUARE";
        String username = "SUPER KID";
        Category category = new Category();
        category.setName("RECTANGLE");
        Shape shape = new Shape(category, shapeName);
        shape.setUsername(username);
        category.setRequirements(Arrays.asList("width", "height"));
        category.setId("2");
        shape.setCategory(category);
        ShapeProperty width = new ShapeProperty("width", "4");
        ShapeProperty height = new ShapeProperty("height", "5");
        Set<ShapeProperty> shapeProperties = new HashSet<>();
        shapeProperties.add(width);
        shapeProperties.add(height);
        shape.setShapeProperties(shapeProperties);
        categoryRepository.save(category);
        shapeRepository.save(shape);

        List<Shape> found = shapeRepository.findByUsername(username);
        assertThat(found.stream().map(shape1 -> shape1.getName()).collect(Collectors.toList())).contains("TOKYO SQUARE");
        assertThat(found.stream().map(shape1 -> shape1.getShapeProperties()).collect(Collectors.toList())).contains(Sets.newHashSet(Arrays.asList(width, height)));
    }
}
