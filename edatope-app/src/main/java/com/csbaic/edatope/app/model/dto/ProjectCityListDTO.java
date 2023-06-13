package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.entity.Dict;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProjectCityListDTO {

    private List<DictDTO> province;

    private List<DictDTO> city;

    private List<Dict> district;
}
