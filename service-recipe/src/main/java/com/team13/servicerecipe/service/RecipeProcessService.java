package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipeProcessDto;
import com.team13.servicerecipe.entity.RecipeProcess;
import com.team13.servicerecipe.repository.RecipeProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeProcessService {
    @Autowired
    private RecipeProcessRepository recipeProcessRepository;

    public RecipeProcess saveProcess(RecipeProcess process) {
        return recipeProcessRepository.save(process);
    }

    public List<RecipeProcess> getProcessByRecipeId(Long recipeId) {
        return recipeProcessRepository.findByRecipeId(recipeId);
    }

    public List<RecipeProcessDto> getProcessDtosByRecipeId(Long recipeId) {
        return getProcessByRecipeId(recipeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RecipeProcessDto convertToDto(RecipeProcess process) {
        return RecipeProcessDto.builder()
                .id(process.getId())
                .imagePath(process.getImagePath())
                .contents(process.getContents())
                .build();
    }

    public RecipeProcess convertToEntity(RecipeProcessDto dto) {
        return RecipeProcess.builder()
                .id(dto.getId())
                .imagePath(dto.getImagePath())
                .contents(dto.getContents())
                .build();
    }
}
