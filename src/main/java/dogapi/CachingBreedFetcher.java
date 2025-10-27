package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Return cached if available
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        try {
            callsMade++;  // increment BEFORE calling underlying fetcher
            List<String> subBreeds = fetcher.getSubBreeds(breed);

            // Cache the result
            cache.put(breed, subBreeds);

            return subBreeds;
        } catch (BreedNotFoundException e) {
            // Do NOT cache failures
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
