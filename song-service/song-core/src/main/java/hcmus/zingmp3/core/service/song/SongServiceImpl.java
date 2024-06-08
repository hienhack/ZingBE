package hcmus.zingmp3.core.service.song;

import hcmus.zingmp3.common.domain.exception.ResourceAlreadyExistsException;
import hcmus.zingmp3.common.domain.model.Song;
import hcmus.zingmp3.common.domain.model.SongStatus;
import hcmus.zingmp3.common.service.song.SongQueryService;
import hcmus.zingmp3.core.web.dto.SongRequest;
import hcmus.zingmp3.core.web.dto.SongResponse;
import hcmus.zingmp3.core.web.dto.mapper.SongMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongQueryService queryService;

    private final SongCommandService commandService;

    private final SongMapper mapper;

    @Override
    public Song getById(UUID id) {
        return queryService.getById(id);
    }

    @Override
    public Song getByAlias(String alias) {
        return queryService.getByAlias(alias);
    }

    @Override
    public boolean existsById(UUID id) {
        return queryService.existsById(id);
    }

    @Override
    public boolean existsByAlias(String alias) {
        return queryService.existsByAlias(alias);
    }

    @Override
    public List<Song> getAll(Pageable pageable) {
        return queryService.getAll(pageable);
    }

    @Override
    public List<Song> getAll() {
        return queryService.getAll();
    }

    @Override
    public void update(Song object) {
        commandService.update(object);
    }

    @Override
    public void delete(Song object) {
        commandService.delete(object);
    }

    @Override
    public void create(Song object) {
        commandService.create(object);
    }


    @Override
    public SongResponse createSong(SongRequest request) {
        // todo: implement this method

        if (queryService.existsByAlias(request.alias())) {
            throw new ResourceAlreadyExistsException(
                    String.format("Song with alias %s already exists", request.alias())
            );
        }

        Song song = mapper.toEntity(request);
        song.setId(UUID.randomUUID());
        song.setStatus(SongStatus.APPROVAL_PENDING);

        create(song);

        return mapper.toDto(song);
    }

    @Override
    public SongResponse updateSong(SongRequest request) {
        // todo: implement this method
        Song song = mapper.toEntity(request);

        update(song);

        return mapper.toDto(song);
    }

    @Override
    public void deleteSong(UUID id) {
        Song song = getById(id);
        delete(song);
    }

    @Override
    public SongResponse getSongById(UUID id) {
        var song = getById(id);
        return mapper.toDto(song);
    }

    @Override
    public SongResponse getSongByAlias(String alias) {
        var song = getByAlias(alias);
        return mapper.toDto(song);
    }

    @Override
    public void approvedSong(String alias) {
        var song = getByAlias(alias);
        song.setStatus(SongStatus.APPROVED);
        update(song);
    }

    @Override
    public void rejectedSong(String alias) {
        var song = getByAlias(alias);
        song.setStatus(SongStatus.REJECTED);
        update(song);
    }

    @Override
    public void releasedSong(String alias) {
        var song = getByAlias(alias);
        song.setStatus(SongStatus.RELEASED);
        update(song);
    }

    @Override
    public List<SongResponse> getAllSongs(Pageable pageable) {
        return mapper.toDto(getAll(pageable));
    }

    @Override
    public List<SongResponse> getAllSongs() {
        return mapper.toDto(getAll());
    }
}
