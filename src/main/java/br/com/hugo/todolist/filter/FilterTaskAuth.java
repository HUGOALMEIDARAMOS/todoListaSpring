package br.com.hugo.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.hugo.todolist.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //pegar a rota que esta recebendo
        var servletpath = request.getServletPath();
        //se quisse que a rota fosse igal usaria => servletpath.equals("/task")
        //Como quero que a rota comece com /task , usa-se conforme abaixo
        if(servletpath.startsWith("/task")){
            //pegar a autenticação user e password
            var autorization = request.getHeader("Authorization");
            var authEncoder = autorization.substring("Basic".length()).trim();
            byte[] authDecoder = Base64.getDecoder().decode(authEncoder);
            var authString = new String(authDecoder);
            String[] credenciais = authString.split(":");
            String username = credenciais[0];
            String password = credenciais[1];

            //valida usuario
            var usuario = this.userRepository.findByUsername(username);

            if(usuario == null){
                response.sendError(401, "Usuário sem autorização");
            }else{
                //valida a senha
                var passwordVerify =  BCrypt.verifyer().verify(password.toCharArray(), usuario.getPassword());
                if(passwordVerify.verified){
                    request.setAttribute("idUser", usuario.getId());
                    filterChain.doFilter(request, response);
                }else{
                    response.sendError(401, "Senha Incorreta");
                }
            }
        }else{
            filterChain.doFilter(request, response);
        }





    }

}
