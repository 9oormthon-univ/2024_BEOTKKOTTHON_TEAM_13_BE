package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipesProcessesDTO;
import com.team13.servicerecipe.entity.RecipesProcesses;
import com.team13.servicerecipe.repository.RecipesProcessesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipesProcessesService {

    @Autowired
    private RecipesProcessesRepository recipesProcessesRepository;

    public RecipesProcesses saveProcess(RecipesProcesses process) {
        return recipesProcessesRepository.save(process);
    }

    public List<RecipesProcesses> getProcessByRecipeId(Long recipeId) {
        return recipesProcessesRepository.findByRecipeId(recipeId);
    }

    public RecipesProcessesDTO convertToDto(RecipesProcesses process) {
        RecipesProcessesDTO dto = new RecipesProcessesDTO();
        dto.setId(process.getId());
        dto.setRecipeId(process.getRecipe().getId());
        dto.setImagePath(process.getImagePath());
        dto.setContents(process.getContents());
        return dto;
    }

    public RecipesProcesses convertToEntity(RecipesProcessesDTO dto) {
        RecipesProcesses process = new RecipesProcesses();
        process.setId(dto.getId());
        process.setImagePath(dto.getImagePath());
        process.setContents(dto.getContents());
        return process;
    }



}
