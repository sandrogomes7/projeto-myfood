package br.ufal.ic.p2.myfood.util;

public enum AttributeTranslation {
    RESTAURANT("restaurante", "restaurant"),
    MARKET("mercado", "market"),
    PHARMACY("farmacia", "pharmacy"),
    NAME("nome", "name"),
    PASSWORD("senha", "password"),
    EMAIL("email", "email"),
    ADDRESS("endereco", "address"),
    KITCHEN_TYPE("tipoCozinha", "kitchenType"),
    MARKET_TYPE("tipoMercado", "marketType"),
    OPEN_24H("aberto24Horas", "open24h"),
    NUMBER_EMPLOYEES("numeroFuncionarios", "numberEmployees"),
    OWNER("dono", "owner"),
    VALUE("valor", "value"),
    COMPANY("empresa", "company"),
    CATEGORY("categoria", "category"),
    STATE("estado", "state"),
    CUSTOMER("cliente", "customer"),
    PRODUCTS("produtos", "products"),
    CPF("cpf", "cpf"),
    VEHICLE("veiculo", "vehicle"),
    PLATE("placa", "plate"),
    ORDER("pedido", "order"),
    DELIVERY_PERSON("entregador", "deliveryPerson"),
    DESTINATION("destino", "destination"),
    OPEN("aberto", "open"),
    PREPARING("preparando", "preparing"),
    READY("pronto", "ready"),
    DELIVERING("entregando", "delivering"),
    DELIVERED("entregue", "delivered"),
    OPENING("abre", "opening"),
    CLOSING("fecha", "closing"),
    ;

    private final String portuguese;
    private final String english;
    private static final String INVALID = "invalid";

    AttributeTranslation(String portuguese, String english) {
        this.portuguese = portuguese;
        this.english = english;
    }

    public String getPortuguese() {
        return portuguese;
    }

    public String getEnglish() {
        return english;
    }

    public static String getEnglishAttribute(String portugueseAttribute) {
        for (AttributeTranslation attribute : values()) {
            if (attribute.getPortuguese().equals(portugueseAttribute)) {
                return attribute.getEnglish();
            }
        }
        return INVALID;
    }

    public static String getPortugueseAttribute(String englishAttribute) {
        for (AttributeTranslation attribute : values()) {
            if (attribute.getEnglish().equals(englishAttribute)) {
                return attribute.getPortuguese();
            }
        }
        return INVALID;
    }
}