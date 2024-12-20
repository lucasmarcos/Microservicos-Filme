package br.edu.utfpr.api_avaliacao.controller;

import br.edu.utfpr.api_avaliacao.dtos.AvaliacaoDTO;
import br.edu.utfpr.api_avaliacao.dtos.MediaDTO;
import br.edu.utfpr.api_avaliacao.model.Avaliacao;
import br.edu.utfpr.api_avaliacao.repository.AvaliacaoRepository;
import br.edu.utfpr.api_avaliacao.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

	private AvaliacaoRepository repository;

	AvaliacaoService avaliacaoService;

	public AvaliacaoController(AvaliacaoRepository repository, AvaliacaoService avaliacaoService) {
		this.repository = repository;
		this.avaliacaoService = avaliacaoService;
	}

	@GetMapping
	public ResponseEntity<List<AvaliacaoDTO>> findAll() {
		List<AvaliacaoDTO> lista = this.repository.findAll().stream().map(avaliacao -> new AvaliacaoDTO(avaliacao.getId(), avaliacao.getTitulo(), avaliacao.getComentario(), avaliacao.getNota(), avaliacao.getFilme(), avaliacao.getUsuario())).toList();

		return ResponseEntity.ok().body(lista);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AvaliacaoDTO> findById(@RequestParam Long id) {
		Avaliacao avaliacao = repository.findById(id).orElse(null);

		if (avaliacao == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new AvaliacaoDTO(avaliacao.getId(), avaliacao.getTitulo(), avaliacao.getComentario(), avaliacao.getNota(), avaliacao.getFilme(), avaliacao.getUsuario()));
		}
	}

	@PostMapping
	public ResponseEntity<String> create(@Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
		if (avaliacaoDTO.nota() < 0 || avaliacaoDTO.nota() > 10) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A Nota deve ser entre 0 e 10");
		}

		/*

        if (avaliacao.getTitulo() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome do filme é obrigatorio");
        }

		*/

		Avaliacao avaliacao = new Avaliacao(avaliacaoDTO.id(), avaliacaoDTO.titulo(), avaliacaoDTO.comentario(), avaliacaoDTO.nota(), avaliacaoDTO.filme(), avaliacaoDTO.usuario());

		this.repository.save(avaliacao);

		return ResponseEntity.status(HttpStatus.CREATED).body("Filme avaliado com sucesso!");
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable Long id, @Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
		Avaliacao avaliacao = this.repository.findById(id).orElse(null);

		if (avaliacao != null) {
			avaliacao.setTitulo(avaliacaoDTO.titulo());
			avaliacao.setComentario(avaliacaoDTO.comentario());
			avaliacao.setNota(avaliacao.getNota());

			this.repository.save(avaliacao);

			return ResponseEntity.status(HttpStatus.OK).body("Avaliação alterada com sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avaliação não encontrada");
		}
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		Avaliacao avaliacao = repository.findById(id).orElse(null);

		if (avaliacao != null) {
			this.repository.delete(avaliacao);

			return ResponseEntity.status(HttpStatus.OK).body("Avaliação deletada com sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avaliação não encontrada");
		}
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<AvaliacaoDTO>> getByTitulo(@PathVariable("titulo") String titulo) {
		List<Avaliacao> lista = this.repository.findByTitulo(titulo);

		if (lista.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

		return ResponseEntity.ok(lista.stream().map(avaliacao -> new AvaliacaoDTO(avaliacao.getId(), avaliacao.getTitulo(), avaliacao.getComentario(), avaliacao.getNota(), avaliacao.getFilme(), avaliacao.getUsuario())).toList());
	}

	@GetMapping("/media/{titulo}")
	public ResponseEntity<MediaDTO> getAverageNotaByTitulo(@PathVariable("titulo") String titulo) {
		Double media = repository.findAverageNotaByTitulo(titulo);

		if (media == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

		MediaDTO mediaDTO = new MediaDTO(titulo, media);

		return ResponseEntity.ok(mediaDTO);
	}

}
