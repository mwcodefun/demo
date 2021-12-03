package code.ss.demo1.collections;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Measurement(iterations = 3)
@Warmup(iterations = 1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MapBenchMark {

     MwMap<String, String> map = new MwMap<>();
     HashMap<String, String> hmap = new HashMap<>();



    @TearDown
    public void check( ) {
    }

    @Setup
    public void setUpMap( ) {
        int sameHash = 123;
        for (int i = 0; i < 1000; i++) {
            hmap.put("12" + i, "123" + i);
            map.put("12" + i, "123" + i);
        }
        for (Map.Entry<String, String> e : hmap.entrySet()) {
        }
    }
    @Benchmark
    public void testHMap( ) {
        SamehashString samehashString = new SamehashString("12" + 1000, 123);
        hmap.get("12999");
    }
    @Benchmark()
    public void testMwMap( ) {
        SamehashString samehashString = new SamehashString("12" + 1000, 123);
        map.get("12999");
    }



    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MapBenchMark.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
//        MapBenchMark mapBenchMark = new MapBenchMark();
//        mapBenchMark.setUpMap();
//        SamehashString samehashString = new SamehashString("12" + 100, 123);
//        long l = System.nanoTime();
//
//        System.out.println(mapBenchMark.hmap.get(samehashString));
//        System.out.println("hashmap:" + (System.nanoTime() - l));
//        l = System.nanoTime();
//
//        System.out.println(mapBenchMark.map.get(samehashString));
//        System.out.println("mw:" + (System.nanoTime() - l));
    }

}
