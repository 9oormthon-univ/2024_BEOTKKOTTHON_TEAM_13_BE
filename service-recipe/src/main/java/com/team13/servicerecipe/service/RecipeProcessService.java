package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipeProcessDto;
import com.team13.servicerecipe.entity.RecipeProcess;
import com.team13.servicerecipe.repository.RecipeProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public RecipeProcessDto convertToDto(RecipeProcess process) {
        RecipeProcessDto dto = new RecipeProcessDto();
        dto.setId(process.getId());
        dto.setRecipeId(process.getRecipe().getId());
        dto.setImagePath(process.getImagePath());
        dto.setContents(process.getContents());
        return dto;
    }

    public RecipeProcess convertToEntity(RecipeProcessDto dto) {
        RecipeProcess process = new RecipeProcess();
        process.setId(dto.getId());
        process.setImagePath(dto.getImagePath());
        process.setContents(dto.getContents());
        return process;
    }
}
