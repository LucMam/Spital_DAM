package ro.dam.spital.dto.workflow;

public class ProgramareResponseDTO {
    private boolean succes;
    private String mesaj;

    public ProgramareResponseDTO(boolean succes, String mesaj) {
        this.succes = succes;
        this.mesaj = mesaj;
    }

    public boolean isSucces() { return succes; }
    public String getMesaj() { return mesaj; }
}
