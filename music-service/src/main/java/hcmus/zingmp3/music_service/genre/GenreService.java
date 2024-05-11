package hcmus.zingmp3.music_service.genre;

import hcmus.zingmp3.music_service.genre.model.Genre;
import hcmus.zingmp3.music_service.genre.model.GenreRequest;
import hcmus.zingmp3.music_service.genre.model.GenreResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface GenreService {
    GenreResponse createGenre(GenreRequest request);
    GenreResponse getGenreById(UUID id);
    List<GenreResponse> getAllGenres(Pageable pageable);
    GenreResponse updateGenre(UUID id, GenreRequest request);
    void deleteGenre(UUID id);
    Genre getOrCreateByAlias(String alias);
}
