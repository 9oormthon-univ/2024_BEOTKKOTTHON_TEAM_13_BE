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

}
