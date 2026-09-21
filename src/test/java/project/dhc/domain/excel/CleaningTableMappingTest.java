package project.dhc.domain.excel;

import jakarta.persistence.Table;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl;
import org.junit.jupiter.api.Test;
import project.dhc.domain.cleaning.CleaningCheck;

import static org.junit.jupiter.api.Assertions.*;

class CleaningTableMappingTest {
    @Test
    void preservesExistingCleaningTableName() {
        String name = CleaningCheck.class.getAnnotation(Table.class).name();
        Identifier physical = new PhysicalNamingStrategySnakeCaseImpl()
                .toPhysicalTableName(Identifier.toIdentifier(name), null);
        assertEquals("btl_CleaningCheck", physical.getText());
        assertTrue(physical.isQuoted());
    }
}
