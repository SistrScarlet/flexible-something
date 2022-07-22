package net.sistr.flexiblesomething.skill;

import java.util.Optional;

public class SkillTree {
    private final Skill root;

    public SkillTree(Skill root) {
        this.root = root;
    }

    public Skill getRoot() {
        return root;
    }

    public static class Builder {
        private Skill.Builder root = new Skill.Builder("");

        public static Builder createBuilder() {
            return new Builder();
        }

        public static Skill.Builder createSkillBuilder(String name) {
            return new Skill.Builder(name);
        }

        public Builder setRoot(Skill.Builder root) {
            this.root = root;
            return this;
        }

        public SkillTree build() {
            var result = root.build();
            result.setUnlocked(true);
            nameDupCheck(result).ifPresent(name -> {
                throw new IllegalArgumentException("ツリーのスキル\"" + name + "\"が重複しています！");
            });
            return new SkillTree(result);
        }

        protected final Optional<String> nameDupCheck(Skill skill) {
            boolean result = nameDupCheck(skill, skill);
            if (result) {
                return Optional.of(skill.getName());
            }
            return skill.getChildren().stream()
                    .filter(s -> nameDupCheck(s).isPresent())
                    .map(Skill::getName).findAny();
        }

        protected final boolean nameDupCheck(Skill base, Skill skill) {
            if (base != skill && base.getName().equals(skill.getName())) {
                return true;
            }
            skill.getChildren().forEach(s -> nameDupCheck(base, s));
            return false;
        }

    }
}
