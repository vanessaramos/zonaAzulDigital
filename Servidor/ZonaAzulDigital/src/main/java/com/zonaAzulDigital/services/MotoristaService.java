/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zonaAzulDigital.services;

import com.google.gson.Gson;
import com.zonaAzulDigital.DAO.DaoMotoristaBD;
import com.zonaAzulDigital.Excecao.CpfException;
import com.zonaAzulDigital.Excecao.DaoException;
import com.zonaAzulDigital.Excecao.LoginException;
import com.zonaAzulDigital.entidades.Motorista;
import com.zonaAzulDigital.interfaces.ModelMotoristaInterface;
import com.zonaAzulDigital.model.ModelMotorista;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Samuel
 */
@Path("/motorista")
public class MotoristaService {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fazerLogin(String json) {

        Response r = Response.serverError().build();

        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();

            ModelMotoristaInterface md = new ModelMotorista(new DaoMotoristaBD());

            try {

                Motorista m = gson.fromJson(json, Motorista.class);
                Motorista motoristaRetorno = md.login(m.getCpf(), m.getSenha());

                motoristaRetorno.setSenha(null);

                String jsonRetorno = gson.toJson(motoristaRetorno);
                r = Response.ok(jsonRetorno).build();

            } catch (LoginException le) {
                r = Response.status(401).build();
            } catch (Exception e) {
                r = Response.serverError().build();
            }
        }
        return r;
    }

    @POST
    @Path("/salvar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response salvar(String json) {

        Response r = Response.serverError().build();

        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();

            ModelMotoristaInterface md = new ModelMotorista(new DaoMotoristaBD());

            try {
                Motorista m = gson.fromJson(json, Motorista.class);
                md.cadastrar(m);
                r = Response.ok().build();

            } catch (CpfException ce) {
                r =  Response.status(400).build();
            } catch (DaoException de) {
                r = Response.status(422).build();
                return r;
            } catch (Exception e) {
                r = Response.serverError().build();
            }

        }

        return r;
    }
    
    @POST
    @Path("/atualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarMotoristaMobile(String json) {

        Response r = Response.serverError().build();

        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();

            ModelMotoristaInterface md = new ModelMotorista(new DaoMotoristaBD());

            try {
                Motorista m = gson.fromJson(json, Motorista.class);
                
                Motorista motoristaRetorno = md.recuperar(m);
                
                motoristaRetorno.setSenha("");
                
                String jsonRetorno = gson.toJson(motoristaRetorno);
                
                r = Response.ok(jsonRetorno).build();

            } catch (DaoException de) {
                return r;
            } catch (Exception e) {
                r = Response.serverError().build();
            }

        }

        return r;
    }
    
    
    
}
