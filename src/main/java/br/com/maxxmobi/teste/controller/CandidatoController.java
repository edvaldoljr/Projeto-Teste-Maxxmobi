package br.com.maxxmobi.teste.controller;

import br.com.maxxmobi.teste.model.CandidadosModel;
import br.com.maxxmobi.teste.repository.CandidatosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatosRepository candidatosRepository;

    // Listar todos os candidatos com ordenação
    @GetMapping("busca/ordenada")
    public ResponseEntity<List<CandidadosModel>> getAll(
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        // Cria um objeto Sort com base nos parâmetros sortBy e direction
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        // Cria um Pageable com o Sort aplicado
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);

        // Busca todos os candidatos com a ordenação definida
        List<CandidadosModel> candidatos = candidatosRepository.findAll(pageable).getContent();

        return candidatos.isEmpty()
                ? ResponseEntity.notFound().build() // Retorna 404 se nenhum candidato for encontrado
                : ResponseEntity.ok(candidatos); // Retorna 200 com a lista de candidatos
    }

    // Buscar Candidato por Id
    @GetMapping ("/{id}")
    public ResponseEntity<CandidadosModel> getById(@PathVariable Long id) {
        return candidatosRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Cadastrar um Candidato
    @PostMapping
    public  ResponseEntity<CandidadosModel> post(@Valid @RequestBody CandidadosModel candidadosModel) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(candidatosRepository.save(candidadosModel));
    }

    //Atualizar o cadastro de um Candidato
    @PutMapping
    public ResponseEntity<CandidadosModel> put(@Valid @RequestBody CandidadosModel candidadosModel) {
        return candidatosRepository.findById(candidadosModel.getId())
                .map(res -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(candidatosRepository.save(candidadosModel)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Deletar um cadastro de Candidato
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        Optional<CandidadosModel> candidadosModel =  candidatosRepository.findById(id);

        if (candidadosModel.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        candidatosRepository.deleteById(id);
    }

    @GetMapping("/buscarPorNome")
    public ResponseEntity<List<CandidadosModel>> buscarPorNome(
            @RequestParam(value = "nome", required = false) String nome) {

        List<CandidadosModel> candidatos = (nome == null || nome.isEmpty())
                ? candidatosRepository.findAll()
                : candidatosRepository.findAllByNomeContainingIgnoreCase(nome);

        return candidatos.isEmpty()
                ? ResponseEntity.notFound().build() // Retorna 404 se nenhum candidato for encontrado
                : ResponseEntity.ok(candidatos); // Retorna 200 com a lista de candidatos
    }

    @GetMapping("/buscarPorNascimento")
    public ResponseEntity<List<CandidadosModel>> buscarPorNascimento(
            @RequestParam(value = "nascimento", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date nascimento) {

        List<CandidadosModel> candidatos = (nascimento == null)
                ? candidatosRepository.findAll()
                : candidatosRepository.findAllByNascimento(nascimento);

        return candidatos.isEmpty()
                ? ResponseEntity.notFound().build() // Retorna 404 se nenhum candidato for encontrado
                : ResponseEntity.ok(candidatos); // Retorna 200 com a lista de candidatos
    }

    @GetMapping("/buscarPorSexo")
    public ResponseEntity<List<CandidadosModel>> buscarPorSexo(
            @RequestParam(value = "sexo", required = false) String sexo) {

        List<CandidadosModel> candidatos = (sexo == null || sexo.isEmpty())
                ? candidatosRepository.findAll()
                : candidatosRepository.findAllBySexoContainingIgnoreCase(sexo);

        return candidatos.isEmpty()
                ? ResponseEntity.notFound().build() // Retorna 404 se nenhum candidato for encontrado
                : ResponseEntity.ok(candidatos); // Retorna 200 com a lista de candidatos
    }

    @GetMapping("/buscarPorNota")
    public ResponseEntity<List<CandidadosModel>> buscarPorNota(
            @RequestParam(value = "nota", required = false) Integer nota) {

        List<CandidadosModel> candidatos = (nota == null)
                ? candidatosRepository.findAll()
                : candidatosRepository.findAllByNota(nota);

        return candidatos.isEmpty()
                ? ResponseEntity.notFound().build() // Retorna 404 se nenhum candidato for encontrado
                : ResponseEntity.ok(candidatos); // Retorna 200 com a lista de candidatos
    }
}
