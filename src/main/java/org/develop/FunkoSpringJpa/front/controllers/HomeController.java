package org.develop.FunkoSpringJpa.front.controllers;

import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.rest.categorias.commons.model.Categoria;
import org.develop.FunkoSpringJpa.rest.categorias.repositories.CategoriaRepository;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoCreateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.dto.FunkoUpdateDto;
import org.develop.FunkoSpringJpa.rest.funko.commons.mainUse.model.Funko;
import org.develop.FunkoSpringJpa.rest.funko.exceptions.FunkoNotFound;
import org.develop.FunkoSpringJpa.rest.funko.repositories.FunkoRepository;
import org.develop.FunkoSpringJpa.rest.funko.services.FunkoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class HomeController {
    private final FunkoService funkoService;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public HomeController(FunkoService funkoService, CategoriaRepository categoriaRepository) {
        this.funkoService = funkoService;
        this.categoriaRepository = categoriaRepository;
    }
    @GetMapping("/")
    public String listFunkos(Model model,
                        @RequestParam(value = "name") Optional<String> name,
                        @RequestParam(value = "quantity") Optional<Integer> quantity,
                        @RequestParam(value = "price") Optional<Double> price,
                        @RequestParam(value = "category") Optional<String> category,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "4") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction) {
        log.info(name.orElse(""));
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Funko> funkosPage = funkoService.getAll(name, quantity,price, category, pageable);
        model.addAttribute("name", name.orElse(""));
        model.addAttribute("funkosPage",funkosPage);
        return "index";
    }

    @GetMapping("/funkos/create")
    public String createFunkoForm(Model model){
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        model.addAttribute("funko", FunkoCreateDto.builder().build());
        return "form-post";
    }

    @PostMapping("/funkos/create")
    public String createFunko(@ModelAttribute("funko") FunkoCreateDto funko){
        System.out.println(funko);
        funkoService.save(funko);
        return "redirect:/";
    }

    @GetMapping("/funkos/update/{id}")
    public String editFunko(Model model, @PathVariable Long id){
        Funko funko = funkoService.findById(id);
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("funko", funko);
        model.addAttribute("categorias",categorias);
        return "form-put";
    }

    @PostMapping("/funkos/update/{id}")
    public String updateFunko(@PathVariable Long id, @ModelAttribute("funko") FunkoUpdateDto funko){
        funkoService.update(id,funko);
        return "redirect:/funkos/gestion";
    }

    @GetMapping("/funkos/updateImg/{id}")
    public String updateImgFunko(Model model, @PathVariable Long id){
        Funko funko = funkoService.findById(id);
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("funko", funko);
        model.addAttribute("categorias",categorias);
        return "form-put-img";
    }

    @PostMapping("/funkos/updateImg/{id}")
    public String updatePathImgFunko(@PathVariable Long id,@RequestParam("file") MultipartFile file){
        funkoService.updateImage(id, file);
        return "redirect:/funkos/gestion";
    }

    @GetMapping("/funkos/gestion")
    public String gestionFunkos(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Funko> funkosPage = funkoService.getAll(Optional.empty(), Optional.empty(),Optional.empty(), Optional.empty(), pageable);
        model.addAttribute("funkosPage",funkosPage);
        return "tables";
    }

    @GetMapping("/funkos/delete/{id}")
    public String deleteFunko(@PathVariable Long id){
        funkoService.findById(id);
        funkoService.deleteById(id);
        return "redirect:/funkos/gestion";
    }
}
