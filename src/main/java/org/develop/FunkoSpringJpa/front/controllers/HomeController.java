package org.develop.FunkoSpringJpa.front.controllers;

import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.repositories.CategoriaRepository;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoNotFound;
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

    @GetMapping("/funkos/{category}")
    public String listFunkosSeccion(Model model, @PathVariable String category){
        Categoria cateFound = categoriaRepository.findByNameCategoryIgnoreCase(category).get();
        List<Funko> funkos = funkoRepository.findAllByCategory(cateFound);
        model.addAttribute("funkos", funkos);
        return "blank";
    }

    @GetMapping("/funkos/create")
    public String createFunko(Model model){
        model.addAttribute("funko", new Funko());
        return "form-post";
    }

    @GetMapping("/funkos/update/{id}")
    public String updateFunko(Model model, @PathVariable Long id){
        Funko funko = funkoRepository.findById(id).orElseThrow(()-> new FunkoNotFound(id));
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("funko", funko);
        model.addAttribute("categorias",categorias);
        return "form-put";
    }

    @GetMapping("/funkos/updateImg")
    public String updateImgFunko(Model model, @PathVariable Long id){
        Funko funko = funkoRepository.findById(id).orElseThrow(()-> new FunkoNotFound(id));
        model.addAttribute("funko", funko);
        return "form-update-img";
    }

    @GetMapping("/funkos/delete")
    public String deleteFunko(Model model){
        List<Funko> funkos = funkoRepository.findAll();
        model.addAttribute("funkos", funkos);
        return "tables";
    }
}
