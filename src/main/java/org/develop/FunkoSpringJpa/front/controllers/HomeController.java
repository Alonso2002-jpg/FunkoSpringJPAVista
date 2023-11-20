package org.develop.FunkoSpringJpa.front.controllers;

import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.repositories.CategoriaRepository;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.repositories.FunkoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {
    private final FunkoRepository funkoRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public HomeController(FunkoRepository funkoRepository, CategoriaRepository categoriaRepository) {
        this.funkoRepository = funkoRepository;
        this.categoriaRepository = categoriaRepository;
    }
    @GetMapping("/")
    public String listFunkos(Model model) {
        List<Funko> funkos = funkoRepository.findAll();
        model.addAttribute("funkos",funkos);
        return "index";
    }

    @GetMapping("/{category}")
    public String listFunkosSeccion(Model model, @PathVariable String category){
        Categoria cateFound = categoriaRepository.findByNameCategoryIgnoreCase(category).get();
        List<Funko> funkos = funkoRepository.findAllByCategory(cateFound);
        model.addAttribute("funkos", funkos);
        return "blank";
    }
}
