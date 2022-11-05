package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    @Autowired
    private TestEntityManager manager;

    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        stations.save(new Station("잠실역"));
        final Station actual = stations.findByName("잠실역");
        assertThat(actual).isNotNull();
    }

    @Test
    void identity() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get();
        assertThat(station1 == station2).isTrue();
    }

    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }

    @Test
    void saveWithLine() {
        final Station station = new Station("잠실역");
        station.setLine(lines.save(new Line("2호선")));
        stations.save(station);
        flushAndClear();
    }

    @Test
    void findByNameWithLine() {
        final Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine()).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        final Line lise = lines.save(new Line("2호선"));
        final Station station = stations.findByName("교대역");
        station.setLine(lise);
        flushAndClear();
    }

    @Test
    void removeLine() {
        final Station station = stations.findByName("교대역");
        station.setLine(null);
        flushAndClear();
    }

    @Test
    void deleteLine() {
        final Station station = stations.findByName("교대역");
        station.setLine(null);
        final Line line = lines.findById(1L).get();
        lines.delete(line);
        flushAndClear();
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }
}