package br.com.clinica.enums;

import br.com.clinica.dao.ConsultaDAO;
import br.com.clinica.model.Consulta;
import br.com.clinica.model.Medico;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.Usuario;
import java.time.LocalDate;
import java.util.List;

public enum FiltroRelatorio {
    PACIENTE {
        @Override
        public List<Consulta> executar(ConsultaDAO dao, Usuario usuario, String perfil, Object valor) {
            Paciente paciente = (Paciente) valor;
            int pacienteId = paciente.getId();
            return "MEDICO".equals(perfil)
                    ? dao.listarPorPacienteEMedico(pacienteId, usuario.getId())
                    : dao.listarPorPaciente(pacienteId);
        }
    },
    MEDICO {
        @Override
        public List<Consulta> executar(ConsultaDAO dao, Usuario usuario, String perfil, Object valor) {
            Medico medico = (Medico) valor;
            int medicoId = medico.getId();
            return "MEDICO".equals(perfil)
                    ? dao.listarPorMedico(usuario.getId())
                    : dao.listarPorMedico(medicoId);
        }
    },
    STATUS {
        @Override
        public List<Consulta> executar(ConsultaDAO dao, Usuario usuario, String perfil, Object valor) {
            StatusConsulta status = (StatusConsulta) valor;
            return "MEDICO".equals(perfil)
                    ? dao.listarPorStatusEMedico(status, usuario.getId())
                    : dao.listarPorStatus(status);
        }
    },
    PERIODO {
        @Override
        public List<Consulta> executar(ConsultaDAO dao, Usuario usuario, String perfil, Object valor) {
            LocalDate[] periodo = (LocalDate[]) valor;
            LocalDate dataInicial = periodo[0];
            LocalDate dataFinal = periodo[1];
            return "MEDICO".equals(perfil)
                    ? dao.listarPorPeriodoEMedico(dataInicial, dataFinal, usuario.getId())
                    : dao.listarPorPeriodo(dataInicial, dataFinal);
        }
    },
    TODOS {
        @Override
        public List<Consulta> executar(ConsultaDAO dao, Usuario usuario, String perfil, Object valor) {
            return "MEDICO".equals(perfil)
                    ? dao.listarPorMedico(usuario.getId())
                    : dao.listarTodos();
        }
    };

    public abstract List<Consulta> executar(ConsultaDAO dao, Usuario usuario, String perfil, Object valor);
}
