package com.ssportal.be.utilility;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.local.LocalBucket;
import io.github.bucket4j.local.LocalBucketBuilder;

import java.time.Duration;

public class RateLimits {

    private LocalBucket bucket;

    private String userKey;

    public RateLimits() {
    }

    public RateLimits(String userKey, long capacity, long period) {
        this.userKey = userKey;
        createBucket(capacity, period);
    }

    public LocalBucket createBucket(long... capacitiesAndPeriods) {
        LocalBucketBuilder builder = Bucket4j.builder();
        for (int i = 0; i < capacitiesAndPeriods.length; ) {
            builder.addLimit(Bandwidth.simple(capacitiesAndPeriods[i], Duration.ofSeconds(capacitiesAndPeriods[i + 1])));
            i = i + 2;
        }
        bucket = builder.build();
        return bucket;
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }

    public long getAvailableTokens() {
        return bucket.getAvailableTokens();
    }

    @Override
    public String toString() {
        return "RateLimits{" +
                "userKey=" + userKey +
                ", bucket='" + bucket + '\'' +
                '}';
    }
}
