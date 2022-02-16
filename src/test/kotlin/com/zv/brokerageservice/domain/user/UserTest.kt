package com.zv.brokerageservice.domain.user

import com.zv.brokerageservice.domain.event.EventType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class UserTest {

    @Test
    fun it_should_throw_name_exception_when_name_contains_invalid_chars() {
        //given
        val name = "T's"

        //when
        val throwable = Assertions.catchThrowable {
            User.create(name, "test", 1234, ".com", "FR")
        }

        //then
        Assertions.assertThat(throwable.message).isEqualTo("name.contains.invalid.chars")
    }

    @Test
    fun it_should_throw_name_exception_when_last_name_contains_invalid_chars() {
        //given
        val lastName = "L's"

        //when
        val throwable = Assertions.catchThrowable {
            User.create("test", lastName, 1234, ".com", "FR")
        }

        //then
        Assertions.assertThat(throwable.message).isEqualTo("name.contains.invalid.chars")
    }

    @Test
    fun it_should_throw_not_valid_range_exception_when_identity_number_length_is_gte_30() {
        //given
        val identityNumber = 93452943534904394

        //when
        val throwable = Assertions.catchThrowable {
            User.create("test", "test", identityNumber, ".com", "FR")
        }

        //then
        Assertions.assertThat(throwable.message).isEqualTo("user.id.is.not.in.valid.range")
    }

    @Test
    fun it_should_throw_not_valid_chars_exception_when_citizen_contains_invalid_chars() {
        //given
        val citizen = "L=x"

        //when
        val throwable = Assertions.catchThrowable {
            User.create("test", "test", 1234, ".com", citizen)
        }

        //then
        Assertions.assertThat(throwable.message).isEqualTo("citizen.contains.invalid.chars")
    }

    @Test
    fun it_should_create_user() {
        //when
        val user = User.create("test", "test", 1234, ".com", "Fr")

        //then
        Assertions.assertThat(user.domainEvents().size).isEqualTo(1)
        Assertions.assertThat(user.domainEvents().first().type).isEqualTo(EventType.USER_CREATED)
    }
}