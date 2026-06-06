package com.novel2script.backend.scene;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OutlineSceneMapper {

    int insertBatch(@Param("scenes") List<OutlineScene> scenes);

    List<OutlineScene> findByProjectIdOrderBySeqNoAsc(@Param("projectId") String projectId);

    Optional<OutlineScene> findByProjectIdAndSceneId(@Param("projectId") String projectId, @Param("sceneId") String sceneId);

    int deleteByProjectId(@Param("projectId") String projectId);
}
