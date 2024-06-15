package hcmus.zingmp3.common.service.artist;

import hcmus.zingmp3.common.domain.exception.ResourceNotFoundException;
import hcmus.zingmp3.common.domain.model.Artist;
import hcmus.zingmp3.common.domain.model.ArtistStatus;
import hcmus.zingmp3.common.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistQueryServiceImpl implements ArtistQueryService {

    private final ArtistRepository repository;

    @Override
    public Artist getByAlias(
            final String alias
    ) {
        return repository.findByAlias(alias)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Artist with alias %s not found", alias)
                ));
    }

    @Override
    public Artist getById(
            final UUID id
    ) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Artist with id %s not found", id)
                ));
    }

    @Override
    public boolean existsByAlias(String alias) {
        return repository.existsByAlias(alias);
    }

    @Override
    public List<Artist> getAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    @Override
    public List<Artist> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Artist> searchArtist(String name, ArtistStatus status, Pageable pageable) {
        return repository.findAllByNameLikeAndStatus(name, status, pageable);
    }
}
