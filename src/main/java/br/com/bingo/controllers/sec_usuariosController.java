package br.com.bingo.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import br.com.bingo.parametros;
import br.com.bingo.models.BingoClientes;
import br.com.bingo.models.BingoUnidades;
import br.com.bingo.models.PortMenus;
import br.com.bingo.models.SecEmpresas;
import br.com.bingo.models.SecPerfis;
import br.com.bingo.models.SecUsuarios;
import br.com.bingo.models.SecUsuariosPerfis;
import br.com.bingo.repository.sec_usuariosRepository;
import br.com.bingo.repository.sec_usuarios_perfisRepository;
import br.com.bingo.repository.bingo_clientesRepository;
import br.com.bingo.repository.bingo_unidadesRepository;
import br.com.bingo.views.UsuariosDestePerfil;
import br.com.bingo.UsuariosLogados;

@RestController
@RequestMapping(value = "/bingo")
@Api("sec_usuariosController")
@CrossOrigin(origins = "*", maxAge = 3600)

public class sec_usuariosController {

	@Autowired
	sec_usuariosRepository sec_usuariosRepository;

	@Autowired
	sec_usuarios_perfisRepository sec_usuarios_perfisRepository;

	@Autowired
	bingo_clientesRepository bingo_clientesRepository;

	@Autowired
	bingo_unidadesRepository bingo_unidadesRepository;

	@Autowired
	PasswordEncoder encoder;

	@GetMapping("/ListarUsuariosPerfilUnidade/NAOUNIDADE")
	@ApiOperation(value = "lista todos os usuarios que tem o acesso a unidade mas ainda n??o s??o unidades")
	public List<SecUsuarios> ListarUsuariosPerfilUnidadeNAOUNIDADE() {

		List<SecUsuarios> listausuarios = null;
		try {
			listausuarios = sec_usuariosRepository.ListarUsuariosPerfilUnidade();
		} catch (Exception e) {
			System.out.println("Erro : " + e);
		}

		return listausuarios;
	}

	@GetMapping("/ListarUsuariosPerfilCliente/NAOCLIENTE")
	@ApiOperation(value = "lista todos os usuarios com acesso de cliente mas que ainda n??o s??o cliente")
	public List<SecUsuarios> ListarUsuariosPerfilClienteNAOCLIENTE() {

		List<SecUsuarios> listausuarios = null;
		try {
			listausuarios = sec_usuariosRepository.ListarUsuariosPerfilCliente();
		} catch (Exception e) {
			System.out.println("Erro : " + e);
		}

		return listausuarios;
	}

	@ResponseBody
	@PostMapping("/ListarUsuariosDestePerfil")
	@ApiOperation(value = "lista todos os usuarios deste perfil")
	public List<SecUsuarios> ListarUsuariosDestePerfil(@RequestBody HashMap<String, Object> JsonIdperfil) {
		int IDPERFIL = (int) JsonIdperfil.get("idperfil");
		List<SecUsuarios> listausuarios = null;
		try {
			listausuarios = sec_usuariosRepository.ListarUsuariosDestePerfil(IDPERFIL);
		} catch (Exception e) {
			System.out.println("Erro : " + e);
		}

		return listausuarios;
	}

	@GetMapping("/ListarUsuariosSemPerfil")
	@ApiOperation(value = "lista todos os usuarios sem perfil")
	public List<SecUsuarios> ListarUsuariosSemPerfil() {

		List<SecUsuarios> listausuarios = null;
		try {
			listausuarios = sec_usuariosRepository.ListarUsuariosSemPerfil();
		} catch (Exception e) {
			System.out.println("Erro : " + e);
		}

		return listausuarios;
	}

	@ResponseBody
	@PostMapping("/VisualizarUsuariosPerfis/IDPERFIL")
	@ApiOperation(value = "lista todos os usu??rios que cont??m este perfil")
	public List<HashMap<String, Object>> VisualizarUsuariosPerfis(@RequestBody HashMap<String, Object> JsonIdperfil) {

		int IDPERFIL = (int) JsonIdperfil.get("IDPERFIL");
		List<Object[]> usuario = null;
		List listausuarios = new ArrayList();

		try {
			usuario = sec_usuariosRepository.VisualizarUsuariosPerfis(IDPERFIL);

			for (Object[] obj : usuario) {

				HashMap<String, Object> objeto = new HashMap<>();

				int idusuarioperfil = (int) obj[0];
				int idusuario = (int) obj[1];
				String nomeusuario = (String) obj[2];
				String email = (String) obj[3];

				objeto.put("IDUSUARIOPERFIL", idusuarioperfil);
				objeto.put("IDUSUARIO", idusuario);
				objeto.put("NOMEUSUARIO", nomeusuario);
				objeto.put("EMAIL", email);

				listausuarios.add(objeto);

			}
		} catch (Exception e) {
			System.out.println("Erro : " + e.getMessage());

		}
		return listausuarios;
	}

	@ResponseBody
	@PostMapping("/BuscarUsuario")
	@ApiOperation(value = "retorna 1 Usuario por IDUSUARIO")
	public SecUsuarios listarsec_usuarioID(@RequestBody HashMap<String, Object> JsonIdusuario) {

		int IDUSUARIO = (int) JsonIdusuario.get("IDUSUARIO");
		SecUsuarios usuario = null;

		try {
			usuario = sec_usuariosRepository.findByIDUSUARIO(IDUSUARIO);
		} catch (Exception e) {
			System.out.println("Erro : " + e.getMessage());

		}
		return usuario;
	}

	@PostMapping("/Usuario")
	@ApiOperation(value = "retorna 1 SecUsuarios por IDUSUARIO")
	public SecUsuarios listarSecUsuarios(@RequestHeader HttpHeaders header) {

		SecUsuarios retorno = null;

		try {
			retorno = UsuariosLogados.BuscarUsuario(header);
		} catch (Exception e) {

			System.out.println("Erro : " + e.getMessage());
		}

		return retorno;
	}

	@GetMapping("SecUsuarios/Usuario")
	@ApiOperation(value = "lista SecUsuarios ativo")
	public List<SecUsuarios> SecUsuariosUsuario(@RequestHeader HttpHeaders headers) {

		System.out.println(headers);

		String token = headers.get("authorization").get(0);

		List<SecUsuarios> lista = new ArrayList<>();

		SecUsuarios retorno = UsuariosLogados.BuscarUsuario(headers);

		if (retorno.getNOMEUSUARIO() == null) {

			List<SecUsuarios> sec_usuarios = sec_usuariosRepository.findByTOKEN(token);

			UsuariosLogados.AdicionarUsuario(sec_usuarios.get(0));

		} else {
			lista.add(retorno);
		}

		return lista;
	}

	@PostMapping("/SalvarUsuario")
	@ApiOperation(value = "cria um novo Usuario")
	public HashMap<String, Object> salvarUsuario(@RequestBody SecUsuarios SecUsuarios,
			@RequestHeader HttpHeaders header) {

		SecUsuarios idempresa = UsuariosLogados.BuscarUsuario(header);

		String validacao = "S";

		HashMap<String, Object> retorno = new HashMap<String, Object>();

		try {
			if (SecUsuarios.getNOMEUSUARIO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getAPELIDO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getCARGO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getEMAIL().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getDDD() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getTELEFONE().length() == 0) {
				validacao = "N";
			}
			if (SecUsuarios.getLOGINUSUARIO().length() == 0) {
				validacao = "N";
			}

			/*
			 * if(SecUsuarios.getSENHA().length() == 0) { validacao = "N"; }
			 */

			if (SecUsuarios.getSTATUS() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getIDUSUARIO() == 0) {
				// ?? PARA CRIAR UM NOVO
				SecUsuarios usuario = new SecUsuarios();

				if (validacao == "S") {

					usuario.setAPELIDO(SecUsuarios.getAPELIDO());
					usuario.setCARGO(SecUsuarios.getCARGO());
					usuario.setDDD(SecUsuarios.getDDD());
					usuario.setEMAIL(SecUsuarios.getEMAIL());
					usuario.setFOTO(
							"https://www.uclg-planning.org/sites/default/files/styles/featured_home_left/public/no-user-image-square.jpg?itok=PANMBJF-");
					usuario.setIDEMPRESA(idempresa.getIDEMPRESA());
					usuario.setIDIDIOMA(1);
					usuario.setTELEFONE(SecUsuarios.getTELEFONE());
					usuario.setNOMEUSUARIO(SecUsuarios.getNOMEUSUARIO());
					usuario.setLOGINUSUARIO(SecUsuarios.getLOGINUSUARIO());
					usuario.setDESCSENHA(SecUsuarios.getSENHA());
					usuario.setSENHA(encoder.encode(SecUsuarios.getSENHA()));
					usuario.setSTATUS(SecUsuarios.getSTATUS());
					usuario.setTOKEN("");

					sec_usuariosRepository.save(usuario);

					retorno.put("validacao", validacao);
					retorno.put("mensagem", "novo usu??rio criado com sucesso !");

				}

			} else {
				SecUsuarios usuario = sec_usuariosRepository.findByIDUSUARIO(SecUsuarios.getIDUSUARIO());

				usuario.setAPELIDO(SecUsuarios.getAPELIDO());
				usuario.setCARGO(SecUsuarios.getCARGO());
				usuario.setDDD(SecUsuarios.getDDD());
				usuario.setTELEFONE(SecUsuarios.getTELEFONE());
				usuario.setSENHA(encoder.encode(SecUsuarios.getSENHA()));
				usuario.setSTATUS(1);
				usuario.setEMAIL(SecUsuarios.getEMAIL());
				usuario.setLOGINUSUARIO(SecUsuarios.getLOGINUSUARIO());
				usuario.setDESCSENHA(SecUsuarios.getSENHA());
				usuario.setNOMEUSUARIO(SecUsuarios.getNOMEUSUARIO());
				sec_usuariosRepository.save(usuario);
				retorno.put("validacao", validacao);
				retorno.put("mensagem", "perfil editado com sucesso !");

			}

		} catch (Exception erro) {
			System.out.println("Erro : " + erro.getMessage());
		}

		return retorno;
	}

	@PostMapping("/CriarCliente")
	@ApiOperation(value = "cria um novo cliente")
	public HashMap<String, Object> CriarCliente(@RequestBody SecUsuarios SecUsuarios,
			/* HashMap<String, Object> IdperfilJson, */ @RequestHeader HttpHeaders header) {

		String validacao = "S";

		// int IDPERFIL = (int)IdperfilJson.get("idperfil");

		HashMap<String, Object> retorno = new HashMap<String, Object>();

		try {
			System.out.println(SecUsuarios.getNOMEUSUARIO());

			if (SecUsuarios.getNOMEUSUARIO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getAPELIDO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getCARGO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getEMAIL().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getDDD() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getTELEFONE().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getSENHA().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getSTATUS() == 0) {
				validacao = "N";
			}

			// ?? PARA CRIAR UM NOVO
			SecUsuarios usuario = new SecUsuarios();

			if (validacao == "S") {

				usuario.setAPELIDO(SecUsuarios.getAPELIDO());
				usuario.setCARGO(SecUsuarios.getCARGO());
				usuario.setDDD(SecUsuarios.getDDD());
				usuario.setEMAIL(SecUsuarios.getEMAIL());
				usuario.setFOTO(
						"https://www.uclg-planning.org/sites/default/files/styles/featured_home_left/public/no-user-image-square.jpg?itok=PANMBJF-");
				usuario.setIDEMPRESA(1);
				usuario.setIDIDIOMA(1);
				usuario.setTELEFONE(SecUsuarios.getTELEFONE());
				usuario.setNOMEUSUARIO(SecUsuarios.getNOMEUSUARIO());
				usuario.setSENHA(encoder.encode(SecUsuarios.getSENHA()));
				usuario.setSTATUS(SecUsuarios.getSTATUS());
				usuario.setTOKEN("");

				sec_usuariosRepository.save(usuario);

				SecUsuariosPerfis usuarioperfil = new SecUsuariosPerfis();

				usuarioperfil.setIDUSUARIO(usuario.getIDUSUARIO());
				usuarioperfil.setIDPERFIL(SecUsuarios.getIDUSUARIO());
				usuarioperfil.setSTATUS(1);

				sec_usuarios_perfisRepository.save(usuarioperfil);

				retorno.put("validacao", validacao);
				retorno.put("mensagem", "o cliente foi criado com sucesso !");

			}

		} catch (Exception erro) {
			System.out.println("Erro : " + erro.getMessage());
		}

		return retorno;
	}

	@PostMapping("EditarUsuario")
	@ApiOperation(value = "editar um  Usuario")
	public HashMap<String, Object> editarUsuario(@RequestBody SecUsuarios SecUsuarios,
			@RequestHeader HttpHeaders header) {

		SecUsuarios idempresa = UsuariosLogados.BuscarUsuario(header);

		String validacao = "S";

		HashMap<String, Object> retorno = new HashMap<String, Object>();

		SecUsuarios usuario = sec_usuariosRepository.findByIDUSUARIO(SecUsuarios.getIDUSUARIO());

		try {

			if (SecUsuarios.getFOTO().length() == 0) {
				validacao = "N";
				// retorno.put("valicao",validacao);
			}

			if (SecUsuarios.getNOMEUSUARIO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getAPELIDO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getCARGO().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getEMAIL().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getDDD() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getTELEFONE().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getSENHA().length() == 0) {
				validacao = "N";
			}

			if (SecUsuarios.getSTATUS() == 0) {
				validacao = "N";
			}

			SecUsuarios validaremail = sec_usuariosRepository.findByEMAIL(SecUsuarios.getEMAIL());
			;

			if (validaremail != null) {
				validacao = "N";
				retorno.put("emailexiste", "S");
				retorno.put("validacao", validacao);
			}

			if (validacao == "S") {

				usuario.setAPELIDO(SecUsuarios.getAPELIDO());
				usuario.setCARGO(SecUsuarios.getCARGO());
				usuario.setDDD(SecUsuarios.getDDD());
				usuario.setEMAIL(SecUsuarios.getEMAIL());
				usuario.setFOTO(
						"https://www.uclg-planning.org/sites/default/files/styles/featured_home_left/public/no-user-image-square.jpg?itok=PANMBJF-");
				usuario.setIDEMPRESA(idempresa.getIDEMPRESA());
				usuario.setIDIDIOMA(1);
				usuario.setTELEFONE(SecUsuarios.getTELEFONE());
				usuario.setNOMEUSUARIO(SecUsuarios.getNOMEUSUARIO());
				usuario.setSENHA(encoder.encode(SecUsuarios.getSENHA()));
				usuario.setSTATUS(SecUsuarios.getSTATUS());

				sec_usuariosRepository.save(usuario);

				retorno.put("validacao", validacao);
				// retorno.put("idperfil", perfil.getIDPERFIL());
			}

		} catch (Exception e) {
			System.out.println("Erro : " + e.getMessage());
		}

		return retorno;
	}

	@GetMapping("/SecUsuarios")
	@ApiOperation(value = "lista todos os SecUsuarios em ordem crescente")
	public List<SecUsuarios> listarsec_usuarios() {
		List retorno = null;
		try {
			retorno = sec_usuariosRepository.findAll();
		} catch (Exception e) {
			System.out.println("Erro" + e.getMessage());
		}
		return retorno;
	}

	@DeleteMapping("/DeletarUsuario")
	@ApiOperation(value = "Exclui um Usuario")
	public HashMap<String, Object> excluiruUuario(@RequestBody HashMap<String, Object> JsonIdusuario) {
		HashMap<String, Object> retorno = new HashMap<String, Object>();

		char validacao = 'A';
		String mensagem = "";

		try {

			int IDUSUARIO = (int) JsonIdusuario.get("idusuario");
			int IDPERFIL = sec_usuarios_perfisRepository.ListarUsuarioPerfilIDUSUARIO(IDUSUARIO).getIDPERFIL();

			if (IDPERFIL == 2) {
				// SE O IDPERFIL FOR 2 E FOR UM CLIENTE
				BingoClientes Cliente = bingo_clientesRepository.BuscarClienteIDUSUARIO(IDUSUARIO);
				if (Cliente == null) {
					sec_usuarios_perfisRepository.DeletarSecUsuarioPerfilPorIDUSUARIO(IDUSUARIO);
					// ENFIM DELETE O USU??RIO PELO SEU ID
					sec_usuariosRepository.deleteById(IDUSUARIO);
					validacao = 'S';
					mensagem = "Usu??rio deletado com sucesso";
				} else {
					int IDCLIENTE = Cliente.getIDCLIENTE();
					// DELETE O CLIENTE PELO O ID DO USUARIO
					List<BingoUnidades> unidades = bingo_unidadesRepository.BingoUnidadesIDCLIENTE(IDCLIENTE);
					// VERIFICANDO SE TEM UNIDADES
					if (unidades.size() > 0) {
						// SE TIVER
						// N??o poder?? excluir
						validacao = 'N';
						mensagem = "N??o ?? poss??vel deletar o usu??rio, pois o mesmo ?? um cliente e possui algumas unidades";
						// A A????O N??O SER?? CONCLU??DA
					} else {

						validacao = 'S';
					}
					if (validacao == 'S') {
						// DELETO O CLIENTE
						bingo_clientesRepository.DeletarBingoClientePorIDUSUARIO(IDUSUARIO);
						sec_usuarios_perfisRepository.DeletarSecUsuarioPerfilPorIDUSUARIO(IDUSUARIO);
						// ENFIM DELETE O USU??RIO PELO SEU ID
						sec_usuariosRepository.deleteById(IDUSUARIO);
						mensagem = "Usu??rio deletado com sucesso";

					}

				}

			} else if (IDPERFIL == 3) {
				// CASO AO CONTR??RIO SE O PERFIL FOR 3 E FOR UMA UNIDADE
				bingo_unidadesRepository.DeletarBingoUnidadesPorIDUSUARIO(IDUSUARIO);
				// DELETE A UNIDADE PELO O ID DO USU??RIO
				// FEITO ISSO, AGORA DELLETE O USUARIO PERFIL DO USUARIO
				sec_usuarios_perfisRepository.DeletarSecUsuarioPerfilPorIDUSUARIO(IDUSUARIO);
				// ENFIM DELETE O USU??RIO PELO SEU ID
				sec_usuariosRepository.deleteById(IDUSUARIO);
				mensagem = "Usu??rio unidade deletado com sucesso";
				validacao = 'S';

			}
			
			retorno.put("mensagem", mensagem);
			retorno.put("validacao", validacao);

		} catch (Exception erro) {
			System.out.println("Erro: " + erro.getMessage());
		}
		return retorno;
	}

	@DeleteMapping("SecUsuarios")
	@ApiOperation(value = "Exclui um SecUsuarios")
	public void excluirsec_usuarios(@RequestBody SecUsuarios SecUsuarios) {
		sec_usuariosRepository.delete(SecUsuarios);
	}

	@PutMapping("SecUsuarios")
	@ApiOperation(value = "atualiza um SecUsuarios")
	public SecUsuarios atualizarsec_usuarios(@RequestBody SecUsuarios SecUsuarios) {
		return sec_usuariosRepository.save(SecUsuarios);
	}

}
