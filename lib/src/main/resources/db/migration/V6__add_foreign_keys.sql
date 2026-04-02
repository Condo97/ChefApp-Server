-- Add foreign keys (use IF NOT EXISTS patterns or ignore errors for idempotency)
-- Note: These may fail if orphaned records exist; clean up first if needed

-- IMPORTANT: These constraints are commented out intentionally.
-- A DBA/operator should uncomment them AFTER verifying no orphaned records exist.
-- Run the following queries first to check for orphans:
--   SELECT r.recipe_id FROM Recipe r LEFT JOIN User_AuthToken u ON r.user_id = u.user_id WHERE u.user_id IS NULL;
--   SELECT i.ingredient_id FROM RecipeMeasuredIngredient i LEFT JOIN Recipe r ON i.recipe_id = r.recipe_id WHERE r.recipe_id IS NULL;
--   SELECT i.instruction_id FROM RecipeInstruction i LEFT JOIN Recipe r ON i.recipe_id = r.recipe_id WHERE r.recipe_id IS NULL;
--   SELECT t.tag_id FROM RecipeTag t LEFT JOIN Recipe r ON t.recipe_id = r.recipe_id WHERE r.recipe_id IS NULL;

-- Recipe belongs to a user
-- ALTER TABLE Recipe ADD CONSTRAINT fk_recipe_user FOREIGN KEY (user_id) REFERENCES User_AuthToken(user_id) ON DELETE CASCADE;

-- Ingredients belong to a recipe
-- ALTER TABLE RecipeMeasuredIngredient ADD CONSTRAINT fk_ingredient_recipe FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) ON DELETE CASCADE;

-- Instructions belong to a recipe
-- ALTER TABLE RecipeInstruction ADD CONSTRAINT fk_instruction_recipe FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) ON DELETE CASCADE;

-- Tags belong to a recipe
-- ALTER TABLE RecipeTag ADD CONSTRAINT fk_tag_recipe FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id) ON DELETE CASCADE;

-- Placeholder so Flyway doesn't skip this migration
SELECT 1;
