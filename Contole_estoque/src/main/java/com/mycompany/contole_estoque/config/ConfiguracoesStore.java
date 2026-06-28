package com.mycompany.contole_estoque.config;

/**
 * Singleton que guarda as configurações gerais do sistema.
 *
 * Por enquanto controla quais TIPOS de produto (Perecível / Não Perecível)
 * estão habilitados para novo cadastro. Por padrão ambos vêm habilitados,
 * já que o sistema sempre trabalhou com os dois tipos.
 *
 * Desabilitar um tipo aqui não remove os produtos desse tipo já cadastrados
 * — apenas impede que NOVOS produtos desse tipo sejam criados, escondendo
 * a opção correspondente no diálogo de cadastro.
 */
public class ConfiguracoesStore {

    private static final ConfiguracoesStore INSTANCE = new ConfiguracoesStore();

    private boolean pereciveisHabilitados    = true;
    private boolean naoPereciveisHabilitados = true;

    private ConfiguracoesStore() {
        // configuração inicial: ambos os tipos habilitados
    }

    public static ConfiguracoesStore get() { return INSTANCE; }

    // --------------------------------------------------------------- acessors
    public boolean isPereciveisHabilitados()    { return pereciveisHabilitados; }
    public boolean isNaoPereciveisHabilitados() { return naoPereciveisHabilitados; }

    /**
     * Habilita ou desabilita o cadastro de produtos perecíveis.
     * Não permite desabilitar os dois tipos ao mesmo tempo, pois o sistema
     * precisa de pelo menos um tipo de produto disponível para cadastro.
     *
     * @return true se a alteração foi aplicada; false se foi bloqueada
     *         (por deixaria os dois tipos desabilitados).
     */
    public boolean setPereciveisHabilitados(boolean habilitado) {
        if (!habilitado && !naoPereciveisHabilitados) {
            return false; // bloqueia: não pode desabilitar os dois tipos
        }
        this.pereciveisHabilitados = habilitado;
        return true;
    }

    /**
     * Habilita ou desabilita o cadastro de produtos não perecíveis.
     * Mesma regra de segurança do método acima: ao menos um tipo precisa
     * permanecer habilitado.
     */
    public boolean setNaoPereciveisHabilitados(boolean habilitado) {
        if (!habilitado && !pereciveisHabilitados) {
            return false; // bloqueia: não pode desabilitar os dois tipos
        }
        this.naoPereciveisHabilitados = habilitado;
        return true;
    }
}