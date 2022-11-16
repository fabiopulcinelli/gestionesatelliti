package it.prova.gestionesatelliti.web.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {
	@Autowired
	private SatelliteService satelliteService;
	
	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}

	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

		if (result.hasErrors())
			return "satellite/insert";
		
		if(satellite.getDataRientro()!=null && satellite.getDataLancio()!=null) {
			if(satellite.getDataRientro().before( satellite.getDataLancio())) {
				result.rejectValue("dataLancio", "satellite.dataLancio.deveessere.minore");
				result.rejectValue("dataRientro", "satellite.dataRientro.deveessere.maggiore");
			}
		}
		
		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}
	
	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}
	
	@PostMapping("/elimina")
	public String elimina(@RequestParam(name = "idSatellite") Long idSatellite, RedirectAttributes redirectAttrs) {

		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		if(satellite.getStato()==StatoSatellite.DISATTIVATO && satellite.getDataLancio()!=null) {
			satelliteService.rimuovi(idSatellite);
			redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		}
		else {
			redirectAttrs.addFlashAttribute("errorMessage", "Delete errata");
		}
		
		return "redirect:/satellite";
	}
	
	@GetMapping("/update/{idSatellite}")
	public String update(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("update_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/update";
	}
	
	@PostMapping("/aggiorna")
	public String aggiorna(@Valid @ModelAttribute("update_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttr) {

		if(satellite.getDataRientro().before( satellite.getDataLancio())) {
			result.rejectValue("dataLancio", "satellite.dataLancio.deveessere.minore");
			result.rejectValue("dataRientro", "satellite.dataRientro.deveessere.maggiore");
		}
		
		if (result.hasErrors())
			return "satellite/update";

		satelliteService.aggiorna(satellite);

		redirectAttr.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@GetMapping("/lancio/{idSatellite}")
	public String lancio(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("launch_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		
		if(satellite.getStato()!=null) {
			return "satellite/search";
		}
		launch(satellite);
		
		return "satellite/lancio";
	}
	
	public void launch(@Valid @ModelAttribute("launch_satellite_attr") Satellite satellite) {
		
		satellite.setDataLancio(new Date());
		satellite.setStato(StatoSatellite.IN_MOVIMENTO);
		satelliteService.aggiorna(satellite);
	}
	
	@GetMapping("/rientro/{idSatellite}")
	public String rientro(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("rientro_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);
		Date data = new Date();
		
		if(satellite.getStato()==null) {
			return "satellite/search";
		}
		renter(satellite);
		return "satellite/rientro";
	}
	
	public void renter(@Valid @ModelAttribute("rientro_satellite_attr") Satellite satellite) {
		
		satellite.setDataRientro(new Date());
		satellite.setStato(StatoSatellite.DISATTIVATO);
		satelliteService.aggiorna(satellite);
	}
	
	@GetMapping("/findLanciatiPiuDi2Anni")
	public ModelAndView findLanciatiPiuDi2Anni() {
		ModelAndView mv = new ModelAndView();
		
		List<Satellite> results = satelliteService.findLanciatiPiuDi2Anni(new Date());
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/findDisattivatiMaNonRientrati")
	public ModelAndView findDisattivatiMaNonRientrati() {
		ModelAndView mv = new ModelAndView();
		
		List<Satellite> results = satelliteService.findDisattivatiMaNonRientrati();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/findRimastiInOrbita10AnniFissi")
	public ModelAndView findRimastiInOrbita10AnniFissi() {
		ModelAndView mv = new ModelAndView();
		
		List<Satellite> results = satelliteService.findRimastiInOrbita10AnniFissi(new Date());
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
}
