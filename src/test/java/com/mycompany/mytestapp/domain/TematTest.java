package com.mycompany.mytestapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.mytestapp.web.rest.TestUtil;

public class TematTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Temat.class);
        Temat temat1 = new Temat();
        temat1.setId(1L);
        Temat temat2 = new Temat();
        temat2.setId(temat1.getId());
        assertThat(temat1).isEqualTo(temat2);
        temat2.setId(2L);
        assertThat(temat1).isNotEqualTo(temat2);
        temat1.setId(null);
        assertThat(temat1).isNotEqualTo(temat2);
    }
}
