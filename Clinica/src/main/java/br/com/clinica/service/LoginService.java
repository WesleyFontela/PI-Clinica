package br.com.clinica.service;

import br.com.clinica.dao.UsuarioDAO;
import br.com.clinica.model.Usuario;
import javax.swing.JOptionPane;

/**
 * Serviço responsável pela autenticação de usuários.
 * <p>
 * Encapsula a lógica de verificação de credenciais usando {@link UsuarioDAO} e
 * fornece mensagens de alerta ao usuário via {@link JOptionPane} em caso de
 * falha na autenticação. 
 *
 * <p>
 * <b>Atenção:</b> em ambientes de produção, evite exibir mensagens de erro
 * sensíveis diretamente da camada de serviço e prefira tratar erros em camadas
 * de apresentação (GUI) ou via exceções customizadas. Além disso, nunca compare
 * nem armazene senhas em texto plano — utilize hashing seguro.
 */
public class LoginService {

    /**
     * DAO utilizado para recuperar usuários por login.
     */
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Autentica um usuário a partir de login e senha fornecidos.
     * <p>
     * Comportamento:
     * <ul>
     * <li>Se não existir usuário com o login informado, exibe uma mensagem e
     * retorna {@code null}.</li>
     * <li>Se a senha informada não corresponder, exibe uma mensagem e retorna
     * {@code null}.</li>
     * <li>Se a autenticação for bem-sucedida, retorna a instância de
     * {@link Usuario}.</li>
     * </ul>     
     *
     * @param login login informado pelo usuário
     * @param senha senha informada pelo usuário
     * @return instância de {@link Usuario} autenticada, ou {@code null} em caso
     * de falha
     */
    public Usuario autenticar(String login, String senha) {
        Usuario usuario = usuarioDAO.buscarPorLogin(login);

        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
            return null;
        }

        if (!usuario.getSenha().equals(senha)) {
            JOptionPane.showMessageDialog(null, "Senha incorreta.");
            return null;
        }

        return usuario;
    }
}
