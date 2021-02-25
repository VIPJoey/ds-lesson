package com.mmc.lesson.member.mapper;

import com.mmc.lesson.member.model.TblMemberInfo;
import com.mmc.lesson.member.model.TblMemberInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblMemberInfoMapper {
    long countByExample(TblMemberInfoExample example);

    int deleteByExample(TblMemberInfoExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(TblMemberInfo record);

    int insertSelective(TblMemberInfo record);

    List<TblMemberInfo> selectByExampleWithBLOBs(TblMemberInfoExample example);

    List<TblMemberInfo> selectByExample(TblMemberInfoExample example);

    TblMemberInfo selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") TblMemberInfo record, @Param("example") TblMemberInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") TblMemberInfo record, @Param("example") TblMemberInfoExample example);

    int updateByExample(@Param("record") TblMemberInfo record, @Param("example") TblMemberInfoExample example);

    int updateByPrimaryKeySelective(TblMemberInfo record);

    int updateByPrimaryKeyWithBLOBs(TblMemberInfo record);

    int updateByPrimaryKey(TblMemberInfo record);

    int batchInsert(@Param("list") List<TblMemberInfo> list);

    int batchInsertSelective(@Param("list") List<TblMemberInfo> list, @Param("selective") TblMemberInfo.Column ... selective);

    int upsert(TblMemberInfo record);

    int upsertSelective(TblMemberInfo record);

    int upsertWithBLOBs(TblMemberInfo record);

    int batchUpsert(@Param("list") List<TblMemberInfo> list);

    int batchUpsertSelective(@Param("list") List<TblMemberInfo> list, @Param("selective") TblMemberInfo.Column ... selective);

    int batchUpsertWithBLOBs(@Param("list") List<TblMemberInfo> list);
}