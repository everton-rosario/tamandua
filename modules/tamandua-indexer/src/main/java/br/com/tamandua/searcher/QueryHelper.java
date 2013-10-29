package br.com.tamandua.searcher;


/**
 * @author Everton Rosario
 */
public class QueryHelper {
    
    public static final String CONNECTOR_AND = " and ";
    public static final String CONNECTOR_OR = " or ";
    
    public enum FieldConfig {
        ARTIST_NAME("artist_name", "^9", "^15"),
        MUSIC_TITLE("music_title", "^3.5", "^6.5"),
        LIRIC_TEXT("text", "^0.3", "^0.9");

        private String fieldName;
        private String boostValue;
        private String exactTermBoost;

        /**
         * @param fieldName
         * @param boostValue
         * @param exactTermBoost
         */
        private FieldConfig(String fieldName, String boostValue, String exactTermBoost) {
            this.fieldName = fieldName;
            this.boostValue = boostValue;
            this.exactTermBoost = exactTermBoost;
        }
        /**
         * @return the fieldName
         */
        public String getFieldName() {
            return fieldName;
        }
        /**
         * @return the boostValue
         */
        public String getBoostValue() {
            return boostValue;
        }
        /**
         * @return the exactTermBoost
         */
        public String getExactTermBoost() {
            return exactTermBoost;
        }
    }
    
    /**
     * 
     * Vamos considerar uma busca com 3 termos (T1, T2, T3)
     * (A:T1^6.3 or A:T2^6.3 or A:T2^6.3) and (M:T1^3.5 or M:T2^3.5 or M:T3^3.5) and (L:T1^0.3 or L:T2^0.3 or L:T3^0.3)
     *
     * Para os Termos de busca, por exemplo os abaixo:
     *
     * Na query:
     * #  What You Got - Akon
     * T1: What
     * T2: You
     * T3: Got
     * T4: Akon
     *
     * A query fica:
     * (artist_name:"What You Got Akon"^15 artist_name:What^6.3 or artist_name:You^6.3 or artist_name:Got^6.3 or artist_name:Akon^6.3) and 
     * (music_title:"What You Got Akon"^9 music_title:What^3.5 or music_title:You^3.5 or music_title:Got^3.5 or music_title:Akon^3.5) and 
     * (text:"What You Got Akon"^0.9 text:What^0.3 or text:You^0.3 or text:Got^0.3 or text:Akon^0.3)
     * 
     * */
    public static String getLuceneQuery(String terms, String splitChars, String termConnector, String fieldConnector, boolean useUniqueTermBoost, FieldConfig... searchedFields) {
        StringBuffer query = new StringBuffer();
        boolean hasToUseParenthesis = searchedFields.length > 1;
        for (int i = 0; i < searchedFields.length; i++) {
            FieldConfig fieldConfig = searchedFields[i];

            // Trata os agrupamentos da query
            if (hasToUseParenthesis) {
                query.append("(");
            }
            
            // Boost do Termo UNICO
            if (useUniqueTermBoost) {
                query.append(fieldConfig.getFieldName()).append(":\"").append(terms).append("\"").append(fieldConfig.getExactTermBoost());
                query.append(termConnector);
            }
            
            String[] splittedTerms = terms.split("["+splitChars+"]");
            
            for (int j = 0; j < splittedTerms.length; j++) {
                query.append(fieldConfig.getFieldName()).append(":\"").append(splittedTerms[j]).append("\"").append(fieldConfig.getBoostValue());
                if (j < splittedTerms.length-1) {
                    query.append(termConnector);
                }
            }
            
            if (hasToUseParenthesis) {
                query.append(")");
                if (i < searchedFields.length-1) {
                    query.append(fieldConnector);
                }
            }
        }
        return query.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(getLuceneQuery("AC/DC dantes inferno", " /", CONNECTOR_OR, CONNECTOR_AND, true, new FieldConfig[] {FieldConfig.ARTIST_NAME, FieldConfig.MUSIC_TITLE, FieldConfig.LIRIC_TEXT}));
    }
}
