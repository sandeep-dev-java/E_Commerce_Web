package Com.E_Commerce.Project.Exception;

public class ResourceNotFoundException extends RuntimeException {
    String resourcename;
    String field;
    String fieldName;
    Long fieldId;

    public  ResourceNotFoundException(String resourcename,String field,String fieldName){
        super(String.format("%s not found with %s : %s",resourcename,field,fieldName));
        this.resourcename=resourcename;
        this.field= field;
        this.fieldName=fieldName;

    }
    public ResourceNotFoundException(String resourcename,String field, Long fieldId){
        super(String.format("%s not found with %s : %d",resourcename,field,fieldId));
        this.resourcename= resourcename;
        this.field=field;
        this.fieldId=fieldId;
    }

    public ResourceNotFoundException() {
    }
}
