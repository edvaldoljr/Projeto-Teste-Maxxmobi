package br.com.maxxmobi.teste.repository;

import java.util.Optional;

import br.com.maxxmobi.teste.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

   Optional<Usuario> findByUsuario(String usuario);

}