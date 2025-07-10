package cineconecta.cineconecta.service.Films;


import cineconecta.cineconecta.model.Films.MovieSearchResult;
import cineconecta.cineconecta.model.Films.TmdbMovieDto;
import cineconecta.cineconecta.model.Films.TmdbMovieSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TmdbService {

    private final CppMovieApiClient cppMovieApiClient;

    public Optional<TmdbMovieSearchResponse> searchMovies(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Optional.empty();
        }

        String genreId = mapGenreToId(query.trim());

        Optional<List<MovieSearchResult>> cppResults;

        if (genreId != null) {
            cppResults = cppMovieApiClient.searchMoviesInCpp(null, genreId, null);
        } else {
            cppResults = cppMovieApiClient.searchMoviesInCpp(query, null, null);
        }

        if (cppResults.isPresent()) {
            List<TmdbMovieDto> tmdbMovieDtos = cppResults.get().stream()
                    .map(msr -> {
                        TmdbMovieDto dto = new TmdbMovieDto();
                        dto.setId(msr.getTmdbId());
                        dto.setTitle(msr.getTitle());
                        dto.setReleaseDate(msr.getYear() + "-01-01");
                        dto.setPosterPath(msr.getImageUrl()); // Ya es URL completa desde C++
                        return dto;
                    })
                    .collect(Collectors.toList());

            TmdbMovieSearchResponse response = new TmdbMovieSearchResponse();
            response.setResults(tmdbMovieDtos);
            response.setTotalResults(tmdbMovieDtos.size());
            response.setPage(1);
            response.setTotalPages(1);
            return Optional.of(response);
        }

        return Optional.empty();
    }

    public Optional<TmdbMovieDto> getMovieDetails(Long tmdbId) {
        Optional<MovieSearchResult> movieDetails = cppMovieApiClient.getMovieDetailsInCpp(tmdbId);

        if (movieDetails.isPresent()) {
            MovieSearchResult msr = movieDetails.get();
            TmdbMovieDto dto = new TmdbMovieDto();
            dto.setId(msr.getTmdbId());
            dto.setTitle(msr.getTitle());
            dto.setReleaseDate(msr.getYear() + "-01-01");
            dto.setOverview(msr.getDescription());
            dto.setPosterPath(msr.getImageUrl().replace("https://image.tmdb.org/t/p/w342", ""));
            dto.setVoteAverage(msr.getGeneralRating() != null ? msr.getGeneralRating().doubleValue() : 0.0);
            dto.setDirector(msr.getDirector());
            dto.setWriters(msr.getWriters());
            dto.setActors(msr.getActors());

            if (msr.getGenres() != null && !msr.getGenres().isEmpty()) {
                dto.setGenres(Arrays.stream(msr.getGenres().split(","))
                        .map(String::trim)
                        .map(TmdbMovieDto.GenreDto::new)
                        .collect(Collectors.toList()));
            }

            return Optional.of(dto);
        }
        return Optional.empty();
    }


    public Optional<TmdbMovieSearchResponse> discoverMovies(String genreId, Integer year) {
        System.out.println("🟣 Buscando películas por género ID: " + genreId + " y año: " + year);

        Optional<List<MovieSearchResult>> cppResults = cppMovieApiClient.searchMoviesInCpp(null, genreId, year);

        if (cppResults.isPresent()) {
            System.out.println("✅ Resultados encontrados: " + cppResults.get().size());

            List<TmdbMovieDto> tmdbMovieDtos = cppResults.get().stream()
                    .map(msr -> {
                        TmdbMovieDto dto = new TmdbMovieDto();
                        dto.setId(msr.getTmdbId());
                        dto.setTitle(msr.getTitle());
                        dto.setReleaseDate(msr.getYear() + "-01-01");
                        dto.setPosterPath(msr.getImageUrl()); // Ya es completa
                        return dto;
                    })
                    .toList();

            TmdbMovieSearchResponse response = new TmdbMovieSearchResponse();
            response.setResults(tmdbMovieDtos);
            response.setTotalResults(tmdbMovieDtos.size());
            response.setPage(1);
            response.setTotalPages(1);

            return Optional.of(response);
        }

        System.out.println("❌ No se encontraron resultados desde C++.");
        return Optional.empty();
    }

    private String mapGenreToId(String genre) {
        return switch (genre.toLowerCase()) {
            case "accion", "acción" -> "28";
            case "aventura" -> "12";
            case "animación" -> "16";
            case "comedia" -> "35";
            case "crimen" -> "80";
            case "documental" -> "99";
            case "drama" -> "18";
            case "familia" -> "10751";
            case "fantasía" -> "14";
            case "historia" -> "36";
            case "terror", "horror" -> "27";
            case "musical" -> "10402";
            case "misterio" -> "9648";
            case "romance" -> "10749";
            case "ciencia ficción", "sci-fi" -> "878";
            case "película de tv", "tv movie" -> "10770";
            case "thriller" -> "53";
            case "bélico", "guerra" -> "10752";
            case "western" -> "37";
            default -> null;
        };
    }
}