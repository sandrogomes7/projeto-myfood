package br.ufal.ic.p2.myfood.model;

public class Market extends Company {
    private String marketType;
    private String opening;
    private String closing;

    public Market() {
    }

    public Market(String companyType, int idOwner, String name, String address, String opening, String closing, String marketType) {
        super(companyType, idOwner, name, address);
        this.opening = opening;
        this.closing = closing;
        this.marketType = marketType;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }
}
