package aua.se.aimodel.dto;

public class AiModelRequestDTO {
    private String text;
    private int maxLength;
    private int numReturnSequences;

    public AiModelRequestDTO(String text){
        this.text = text;
        maxLength = 50;
        numReturnSequences = 1;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getNumReturnSequences() {
        return numReturnSequences;
    }

    public void setNumReturnSequences(int numReturnSequences) {
        this.numReturnSequences = numReturnSequences;
    }
}

