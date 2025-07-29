package com.example.Transfero.repository;

import com.example.Transfero.model.FileMeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetaRepository extends MongoRepository<FileMeta,String> {
    Optional<FileMeta> findByShortCode(String shortCode);
}
