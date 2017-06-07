package br.com.productrestfulapi.acceptancetests;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by juliano on 06/06/17.
 */

@Path("/product")
public class ProductResource {

    // http://docs.oracle.com/javaee/6/tutorial/doc/gilik.html

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public JSONObject get() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("produto","produto");
        return jsonObject;
    }


    /*@GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("medico/{crmOuNome}/{uf}")
    public List<Medico> buscarMedico(@PathParam("crmOuNome") String crmOuNome, @PathParam("uf") String uf) throws JSONException {
        try {
            List<Medico> medicos = repositorio.buscarMedico(crmOuNome, uf);
            if (medicos != null && medicos.size() > 0) {
                return medicos;
            }
            JSONObject messageReturn = new JSONObject();
            messageReturn.put("messageReturn", "Medico nao encontrado.");
            throw new NotFoundException(messageReturn);
        } catch (SQLException e) {
            throw internalServerError(e, "Erro ao pesquisar Medico");
        } catch (JSONException e) {
            throw internalServerError(e, "Erro ao pesquisar Medico");
        } finally {
            close(connection);
        }
    }*/
}
