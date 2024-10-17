package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.company.EmpresaNaoExiste;
import br.ufal.ic.p2.myfood.exception.company.NaoEhMercadoValido;
import br.ufal.ic.p2.myfood.exception.company.*;
import br.ufal.ic.p2.myfood.model.*;
import br.ufal.ic.p2.myfood.repository.Data;
import br.ufal.ic.p2.myfood.util.AttributeTranslation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompanyService {
    private final Map<Integer, Company> companyList;
    private final Map<Integer, User> userList;

    private static final String INVALID = "invalid";
    private static final String RESTAURANT = "restaurant";
    private static final String MARKET = "market";
    private static final String PHARMACY = "pharmacy";
    private static final String REGEX_HORAS = "^([0-9][0-9]):[0-9][0-9]$";
    private static final String OWNER_QUALIFICATION = "Company Owner";
    private static final String DELIVERY_QUALIFICATION = "Delivery Person";
    private static final String CUSTOMER_QUALIFICATION = "Customer";

    public CompanyService(Data data) {
        this.companyList = data.getCompanyList();
        this.userList = data.getUserList();
    }

    public int createCompanyRestaurant(String companyType, int idOwner, String companyName, String address, String cuisineType) throws Exception {
        if (cuisineType == null || cuisineType.isEmpty()) throw new TipoCozinhaInvalidoException();

        validateCompanyFields(companyType, idOwner, companyName, address);

        Restaurant restaurant = new Restaurant(RESTAURANT, idOwner, companyName, address, cuisineType);
        companyList.put(restaurant.getId(), restaurant);
        return restaurant.getId();
    }

    public int createCompanyMarket(String companyType, int idOwner, String companyName, String address, String opening, String closing, String marketType) throws Exception {
        if (opening == null || closing == null) throw new HorarioInvalidoException();
        if (marketType == null || marketType.isEmpty()) throw new TipoMercadoInvalidoException();
        if (!opening.matches(REGEX_HORAS) || !closing.matches(REGEX_HORAS)) throw new FormatoDeHoraInvalidoException();
        if (validTime(closing)) throw new HorarioInvalidoException();

        validateCompanyFields(companyType, idOwner, companyName, address);

        Market market = new Market(MARKET, idOwner, companyName, address, opening, closing, marketType);
        companyList.put(market.getId(), market);
        return market.getId();
    }

    public int createCompanyPharmacy(String companyType, int idOwner, String companyName, String address, Boolean open24Hours, Integer numberOfEmployees) throws Exception {
        validateCompanyFields(companyType, idOwner, companyName, address);

        Pharmacy pharmacy = new Pharmacy(PHARMACY, idOwner, companyName, address, open24Hours, numberOfEmployees);
        companyList.put(pharmacy.getId(), pharmacy);
        return pharmacy.getId();
    }

    public String listCompaniesByOwner(int idOwner) throws Exception {
        User user = userList.get(idOwner);
        if (user == null) throw new UsuarioNaoCadastradoException();
        if (!user.userQualification().equals(OWNER_QUALIFICATION)) throw new UsuarioNaoPodeCriarEmpresaException();

        return companyList.entrySet().stream()
                .filter(entry -> entry.getValue().getIdOwner() == idOwner)
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> String.format("[%s, %s]", entry.getValue().getName(), entry.getValue().getAddress()))
                .collect(Collectors.joining(", ", "{[", "]}"));
    }

    public String getCompanyAttribute(int idCompany, String attribute) throws Exception {
        if (attribute == null) throw new AtributoInvalidoException();

        Company company = companyList.get(idCompany);
        if (company == null) throw new EmpresaNaoCadastradaException();

        String englishAttribute = AttributeTranslation.getEnglishAttribute(attribute);
        return switch (englishAttribute) {
            case "name" -> company.getName();
            case "owner" -> userList.get(company.getIdOwner()).getName();
            case "address" -> company.getAddress();
            case "kitchenType" -> getCuisineType(company);
            case "opening" -> getMarketOpening(company);
            case "closing" -> getMarketClosing(company);
            case "marketType" -> getMarketType(company);
            case "open24h" -> getPharmacyOpen24Hours(company);
            case "numberEmployees" -> getPharmacyNumberOfEmployees(company);
            default -> throw new AtributoInvalidoException();
        };
    }

    public int getIdCompany(int idOwner, String companyName, int index) throws Exception {
        if (companyName == null || companyName.isEmpty()) throw new NomeInvalidoException();
        if (index < 0) throw new IndiceInvalidoException();

        User user = userList.get(idOwner);
        if (user == null) throw new UsuarioNaoCadastradoException();

        List<Company> companiesOwner = companyList.entrySet().stream()
                .filter(entry -> entry.getValue().getIdOwner() == idOwner)
                .filter(entry -> entry.getValue().getName().equals(companyName))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();

        if (companiesOwner.isEmpty()) throw new NomeEmpresaNaoExisteException();
        if (companiesOwner.size() <= index) throw new IndiceMaiorQueEsperadoException();

        return companiesOwner.get(index).getId();
    }

    public void changeMarketHours(int idMarket, String opening, String closing) throws Exception {
        Company company = companyList.get(idMarket);
        if (!company.getCompanyType().equals(MARKET)) throw new NaoEhMercadoValido();

        Market market = (Market) company;

        if (opening == null || closing == null) throw new HorarioInvalidoException();
        if (!opening.matches(REGEX_HORAS) || !closing.matches(REGEX_HORAS)) throw new FormatoDeHoraInvalidoException();
        if (validTime(closing)) throw new HorarioInvalidoException();

        market.setOpening(opening);
        market.setClosing(closing);
    }

    public void registerDeliveryPerson(int idCompany, int idDeliveryPerson) throws Exception {
        Company company = companyList.get(idCompany);
        if (company == null) throw new EmpresaNaoCadastradaException();

        DeliveryPerson user = userList.values().stream()
                .filter(u -> u.userQualification().equals(DELIVERY_QUALIFICATION))
                .map(u -> (DeliveryPerson) u)
                .filter(u -> u.getId() == idDeliveryPerson)
                .findFirst()
                .orElseThrow(UsuarioNaoEhUmEntregadorException::new);

        company.addDeliveryPerson(user);
        user.addCompany(company);
    }

    public String getDeliveryPersons(int idCompany) throws Exception {
        Company company = companyList.get(idCompany);
        if (company == null) throw new EmpresaNaoExiste();

        return company.deliveriesPersonInString();
    }

    private void validateCompanyFields(String companyType, int idOwner, String companyName, String address) throws Exception {
        companyType = AttributeTranslation.getEnglishAttribute(companyType);

        if (companyType.equals(INVALID) || (!companyType.equals(RESTAURANT) && !companyType.equals(MARKET) && !companyType.equals(PHARMACY)))
            throw new TipoEmpresaInvalidoException();

        if (companyName == null || companyName.isEmpty()) throw new NomeInvalidoException();
        if (address == null || address.isEmpty()) throw new EnderecoEmpresaInvalidoException();

        for (Company company : companyList.values()) {
            if (company.getIdOwner() != idOwner && company.getName().equalsIgnoreCase(companyName))
                throw new EmpresaComNomeJaExisteException();
            if (company.getIdOwner() == idOwner && company.getAddress().equals(address) && company.getName().equals(companyName))
                throw new EmpresaMesmoNomeELocalException();
        }

        User user = userList.get(idOwner);
        if (user == null) throw new UsuarioNaoCadastradoException();
        if (user.userQualification().equals(CUSTOMER_QUALIFICATION)) throw new UsuarioNaoPodeCriarEmpresaException();
    }

    private static boolean validTime(String time) {
        String[] parts = time.split(":");
        if (parts.length != 2) return true;
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours < 6 || hours > 23) || (minutes < 0 || minutes > 59);
    }

    private String getCuisineType(Company company) throws AtributoInvalidoException {
        if (company.getCompanyType().equals(RESTAURANT)) return ((Restaurant) company).getCuisineType();
        throw new AtributoInvalidoException();
    }

    private String getMarketOpening(Company company) throws AtributoInvalidoException {
        if (company.getCompanyType().equals(MARKET)) return ((Market) company).getOpening();
        throw new AtributoInvalidoException();
    }

    private String getMarketClosing(Company company) throws AtributoInvalidoException {
        if (company.getCompanyType().equals(MARKET)) return ((Market) company).getClosing();
        throw new AtributoInvalidoException();
    }

    private String getMarketType(Company company) throws AtributoInvalidoException {
        if (company.getCompanyType().equals(MARKET)) return ((Market) company).getMarketType();
        throw new AtributoInvalidoException();
    }

    private String getPharmacyOpen24Hours(Company company) throws AtributoInvalidoException {
        if (company.getCompanyType().equals(PHARMACY)) return ((Pharmacy) company).getOpen24Hours().toString();
        throw new AtributoInvalidoException();
    }

    private String getPharmacyNumberOfEmployees(Company company) throws AtributoInvalidoException {
        if (company.getCompanyType().equals(PHARMACY)) return ((Pharmacy) company).getNumberOfEmployees().toString();
        throw new AtributoInvalidoException();
    }
}