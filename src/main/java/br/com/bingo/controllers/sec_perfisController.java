
package br.com.bingo.controllers;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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
import br.com.bingo.models.SecPerfis;
import br.com.bingo.models.SecUsuarios;
import br.com.bingo.repository.sec_perfisRepository;
import br.com.bingo.UsuariosLogados;

@RestController
@RequestMapping(value="/bingo")
@Api("sec_perfisController")
@CrossOrigin(origins="*")

public class sec_perfisController {

	@Autowired
	sec_perfisRepository sec_perfisRepository;
	
	@PostMapping("/SecPerfis")
	@ApiOperation(value="lista todos os SecPerfis em ordem crescente")
	public HashMap<String,Object> listarsec_perfis(@RequestBody HashMap<String,Object> PaginacaoParametros){
		
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		
		
		int INICIO = (int)PaginacaoParametros.get("PAGINA");
		int TAMANHO = (int)PaginacaoParametros.get("TAMANHO");
		
		List<SecPerfis> listaperfis = null;
		try {
			Pageable pagina = PageRequest.of(INICIO, TAMANHO, Sort.by("IDPERFIL").ascending());
			listaperfis = sec_perfisRepository.ListarSecPerfisPaginacao(pagina);
			retorno.put("lista", listaperfis);
			int total = sec_perfisRepository.findAll().size();
			int quantidade = listaperfis.size();
			retorno.put("quantidade", quantidade);
			retorno.put("total", total);
			
		}catch(Exception e) {
			System.out.println("Erro : " + e);
		}
		
		return retorno;	
	}
	
	@ResponseBody
	@PostMapping("/BuscarPerfil")
	@ApiOperation(value="retorna 1 SecPerfis por IDPERFIL")
	public SecPerfis listarsec_perfisID(@RequestBody HashMap<String,Object> JsonIdperfil){
		
		int IDPERFIL = (int)JsonIdperfil.get("IDPERFIL");
		SecPerfis perfil = null;
		
		try {
			perfil = sec_perfisRepository.findByIDPERFIL(IDPERFIL);
		}catch(Exception e) {
			System.out.println("Erro : " + e.getMessage());
			
		}
		return perfil;
	}

	
	@PostMapping("/SalvarPerfil")
	@ApiOperation(value="editar SecPerfis")
	public HashMap<String, Object> EditarSecPerfis(@RequestBody SecPerfis SecPerfis, @RequestHeader HttpHeaders header) {	
		
		HashMap<String, Object> retorno = new HashMap<String,Object>();
		
		String validacao = "S";
		
		SecUsuarios usuario = UsuariosLogados.BuscarUsuario(header);
		
	
		String foto = "https://cdn.pixabay.com/photo/2015/03/04/22/35/head-659652_960_720.png";
		
		try {
			
			if(SecPerfis.getNOMEPERFIL().length() == 0) {
				
				validacao = "N";
			}
			
			if(SecPerfis.getSTATUS() == 0) {
				validacao = "N";
			}
			
			if(SecPerfis.getFOTO().length() == 0) {
				validacao = "N";
			}

		
			//SE NAO TIVER IDPERFIL, CRIE UM NOVO PERFIL
			
			if(SecPerfis.getIDPERFIL() == 0) {
				
				SecPerfis perfil = new SecPerfis();
				
				if(validacao == "S") {
					
				perfil.setFOTO(foto);
				perfil.setIDEMPRESA(usuario.getIDEMPRESA());
				perfil.setNOMEPERFIL(SecPerfis.getNOMEPERFIL());
				perfil.setSTATUS(SecPerfis.getSTATUS());
				sec_perfisRepository.save(perfil);
				
				retorno.put("validacao", "S");
				retorno.put("mensagem", "novo perfil criado com sucesso !");
				retorno.put("idperfil", perfil.getIDPERFIL());
				}

				
			}else {
				//SE TIVER IDPERFIL, PEGUE O PERFIL POR IDPERFIL E ATUALIZE
				SecPerfis perfil = sec_perfisRepository.findByIDPERFIL(SecPerfis.getIDPERFIL());
				
				if(validacao == "S") {
					
					perfil.setFOTO(foto);
					perfil.setIDEMPRESA(usuario.getIDEMPRESA());
					perfil.setNOMEPERFIL(SecPerfis.getNOMEPERFIL());
					perfil.setSTATUS(SecPerfis.getSTATUS());
					sec_perfisRepository.save(perfil);
					
					retorno.put("validacao", "S");
					retorno.put("idperfil", perfil.getIDPERFIL());
					retorno.put("mensagem", "seu perfil foi atualizado com sucesso !");
					}

			}

		}catch(Exception e) {
				System.out.println("Erro : "+e.getMessage());
		}
		
		
			
		
		return retorno;
	}


	
	@DeleteMapping("/DeletarPerfil")
	@ApiOperation(value="Exclui um SecPerfiL")
	public HashMap<String,String> excluirperfil(@RequestBody HashMap<String,Object>JsonIdperfil) {	
		HashMap<String,String> retorno = new  HashMap<String,String>();
		try {
			
			int IDPERFIL = (int)JsonIdperfil.get("idperfil");
			sec_perfisRepository.deleteById(IDPERFIL);
			retorno.put("mensagem", "OK");
			
		}catch(Exception erro){
				System.out.println("Erro: "+erro.getMessage());
		}
		return retorno;
	}


	@PutMapping("SecPerfis")
	@ApiOperation(value="atualiza um SecPerfis")
	public SecPerfis atualizarsec_perfis(@RequestBody SecPerfis SecPerfis) {	
		
		SecPerfis perfil = new SecPerfis();	
		
		int proximo = 0;
		proximo=sec_perfisRepository.proximo();
		
		perfil.setIDPERFIL(proximo);
		
		return sec_perfisRepository.save(perfil);
	}
	
}
