package com.github.beam.tasks;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.util.Arrays;


public class WordCountApp {
    public static interface WordCountOptions extends PipelineOptions {
        @Description("Path of the file to read from")
        @Default.String("src/main/resources/kinglear.txt")
        String getInputFile();

        void setInputFile(String value);

    }

    public static void main(String[] args) {
        WordCountOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(WordCountOptions.class);
        Pipeline p = Pipeline.create(options);
        p.apply(TextIO.read().from(options.getInputFile()))
            .apply(FlatMapElements.into(TypeDescriptors.strings())
                //any letter from any language
                .via((String row) -> Arrays.asList(row.split("[^\\p{L}]+")))
            ).apply(Filter.by((String w) -> !w.isEmpty()))
            .apply(Count.perElement())
            .apply(MapElements.into(TypeDescriptors.strings()).via((KV<String, Long> words) ->
                words.getKey() + ":" + words.getValue()
            ))
            .apply(TextIO.write().to("out/words.txt"))
        ;
        p.run();
    }
}


